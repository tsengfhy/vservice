package com.tsengfhy.vservice.basic.config;

import com.tsengfhy.vservice.basic.annotation.tag.Token;
import com.tsengfhy.vservice.basic.properties.Properties;
import com.tsengfhy.vservice.basic.repository.*;
import com.tsengfhy.vservice.basic.security.access.AdminVoter;
import com.tsengfhy.vservice.basic.security.access.FilterInvocationSecurityMetadataSourceImpl;
import com.tsengfhy.vservice.basic.security.authentication.*;
import com.tsengfhy.vservice.basic.security.core.*;
import com.tsengfhy.vservice.basic.security.verify.DefaultVerifyCodeStrategy;
import com.tsengfhy.vservice.basic.security.verify.VerifyCodeStrategy;
import com.tsengfhy.vservice.basic.security.web.ComplexAuthenticationFilter;
import com.tsengfhy.vservice.basic.service.ISecurityInitService;
import com.tsengfhy.vservice.basic.service.impl.SecurityInitServiceImpl;
import com.tsengfhy.vservice.basic.template.SecurityTemplate;
import com.tsengfhy.vservice.basic.template.impl.JdbcSecurityTemplate;
import com.tsengfhy.vservice.basic.utils.DataUtils;
import com.tsengfhy.vservice.basic.utils.MessageSourceUtils;
import com.tsengfhy.vservice.basic.utils.RestUtils;
import com.tsengfhy.vservice.basic.utils.WebUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.*;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.access.vote.RoleVoter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.*;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.rememberme.AbstractRememberMeServices;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.security.web.authentication.session.*;
import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;
import org.springframework.security.web.session.InvalidSessionStrategy;
import org.springframework.security.web.session.SessionInformationExpiredEvent;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;

import javax.annotation.PostConstruct;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Slf4j
@Configuration
@ConditionalOnClass({AbstractSecurityWebApplicationInitializer.class, SessionCreationPolicy.class, JpaRepository.class})
@ConditionalOnWebApplication
@AutoConfigureBefore({SecurityAutoConfiguration.class})
@AutoConfigureAfter({JPAConfig.class})
@Import({Oauth2Config.class})
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @PostConstruct
    public void init() {
        log.info("VService module [Security] is loaded");
    }

    @Autowired
    private Properties properties;

    @Autowired(required = false)
    private VerifyCodeStrategy verifyCodeStrategy;

    @Autowired
    private SysTokenRepository sysTokenRepository;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        List<String> publicUrlList = new ArrayList<>();
        publicUrlList.add("/static/**");
        publicUrlList.add(properties.getWeb().getLogin());
        publicUrlList.add(properties.getWeb().getFailure());
        publicUrlList.add(properties.getWeb().getExpired());
        publicUrlList.add(properties.getWeb().getTimeout());
        publicUrlList.add(properties.getWeb().getLogout());
        publicUrlList.addAll(properties.getSecurity().getAuthentication().getPublicUrls());

        List<String> deleteCookieList = new ArrayList<>();
        deleteCookieList.add(properties.getSecurity().getSession().getCookie());
        deleteCookieList.addAll(properties.getSecurity().getAuthentication().getDeleteCookies());

        http
                .addFilterAt(authenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .sessionManagement()
                .invalidSessionStrategy(invalidSessionStrategy())
                .maximumSessions(properties.getSecurity().getSession().getMaximumSessions())
                .maxSessionsPreventsLogin(properties.getSecurity().getSession().isMaxSessionsPreventsLogin())
                .expiredSessionStrategy(sessionInformationExpiredStrategy())
                .sessionRegistry(sessionRegistry())
                .and()
                .and()
                .exceptionHandling()
                .accessDeniedHandler(accessDeniedHandler())
                .and()
                .addFilterAt(filterSecurityInterceptor(), FilterSecurityInterceptor.class)
                .authorizeRequests()
                .antMatchers(publicUrlList.toArray(new String[publicUrlList.size()])).permitAll()
                .anyRequest().authenticated()
                .and()
                .logout()
                .logoutUrl(properties.getSecurity().getAuthentication().getLogoutUrl())
                .logoutSuccessHandler(logoutSuccessHandler())
                .invalidateHttpSession(true)
                .deleteCookies(deleteCookieList.toArray(new String[deleteCookieList.size()]))

                .and()
                .formLogin()
                .loginPage(properties.getWeb().getLogin())
                .and()
                .httpBasic()
                .and()
                .csrf().disable()
        ;

        if (properties.getSecurity().getAuthentication().getMobile().isEnabled()) {
            http.authenticationProvider(mobileAuthenticationProvider());
        }

        if (properties.getSecurity().getAuthentication().getToken().isEnabled()) {
            http.authenticationProvider(tokenAuthenticationProvider());
        }

        if (properties.getSecurity().getRememberMe().isEnabled()) {
            http
                    .rememberMe()
                    .rememberMeServices(rememberMeServices())
                    .key(properties.getArtifact() + "-key")
                    .and()
                    .logout()
                    .addLogoutHandler(rememberMeServices())
            ;
        }

        if (properties.getWeb().isSslOnly()) {
            http.requiresChannel().anyRequest().requiresSecure();
        }
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        if (properties.getSecurity().getAdmin().isEnabled()) {
            auth
                    .inMemoryAuthentication().passwordEncoder(passwordEncoder())
                    .withUser(properties.getSecurity().getAdmin().getUsername()).password(passwordEncoder().encode(properties.getSecurity().getAdmin().getPassword())).roles(properties.getSecurity().getAdmin().getRole());
        }
        auth.userDetailsService(usernamePasswordUserService()).passwordEncoder(passwordEncoder());

        SecurityContextHolder.setStrategyName(SecurityContextHolder.MODE_INHERITABLETHREADLOCAL);
    }

    private AbstractAuthenticationProcessingFilter authenticationFilter() throws Exception {
        ComplexAuthenticationFilter authenticationFilter = new ComplexAuthenticationFilter(properties);

        authenticationFilter.setAuthenticationBuilder(authenticationBuilder());
        authenticationFilter.setAuthenticationManager(authenticationManagerBean());

        ChangeSessionIdAuthenticationStrategy changeSessionIdAuthenticationStrategy = new ChangeSessionIdAuthenticationStrategy();
        RegisterSessionAuthenticationStrategy registerSessionStrategy = new RegisterSessionAuthenticationStrategy(sessionRegistry());
        ConcurrentSessionControlAuthenticationStrategy concurrentSessionControlStrategy = new ConcurrentSessionControlAuthenticationStrategy(sessionRegistry());
        concurrentSessionControlStrategy.setMaximumSessions(properties.getSecurity().getSession().getMaximumSessions());
        concurrentSessionControlStrategy.setExceptionIfMaximumExceeded(properties.getSecurity().getSession().isMaxSessionsPreventsLogin());

        List<SessionAuthenticationStrategy> delegateStrategies = new ArrayList<SessionAuthenticationStrategy>();
        delegateStrategies.addAll(Arrays.asList(concurrentSessionControlStrategy, changeSessionIdAuthenticationStrategy, registerSessionStrategy));

        authenticationFilter.setSessionAuthenticationStrategy(new CompositeSessionAuthenticationStrategy(delegateStrategies));


        if (properties.getSecurity().getRememberMe().isEnabled()) {
            authenticationFilter.setRememberMeServices(rememberMeServices());
        }

        authenticationFilter.setAuthenticationSuccessHandler(authenticationSuccessHandler());
        authenticationFilter.setAuthenticationFailureHandler(authenticationFailureHandler());

        return authenticationFilter;
    }

    @Bean
    public AuthenticationBuilder authenticationBuilder() throws Exception {
        List<AuthenticationBuilder> delegateBuilders = new ArrayList<AuthenticationBuilder>();
        delegateBuilders.add(new UsernamePasswordAuthenticationBuilder(properties));
        if (properties.getSecurity().getAuthentication().getMobile().isEnabled()) {
            delegateBuilders.add(new MobileAuthenticationBuilder(properties));
        }
        if (properties.getSecurity().getAuthentication().getToken().isEnabled()) {
            delegateBuilders.add(new TokenAuthenticationBuilder(properties));
        }

        return new CompositeAuthenticationBuilder(delegateBuilders);
    }

    @Bean(name = "authenticationManager")
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    @ConditionalOnProperty(value = "vservice.security.authentication.mobile.enabled", havingValue = "true")
    public AuthenticationProvider mobileAuthenticationProvider() throws Exception {

        MobileAuthenticationProvider mobileAuthenticationProvider = new MobileAuthenticationProvider();
        mobileAuthenticationProvider.setUserDetailsService(mobileUserService());
        Optional.ofNullable(verifyCodeStrategy).ifPresent(mobileAuthenticationProvider::setVerifyCodeStrategy);

        return mobileAuthenticationProvider;
    }

    @Bean
    @ConditionalOnProperty(value = "vservice.security.authentication.token.enabled", havingValue = "true")
    public AuthenticationProvider tokenAuthenticationProvider() throws Exception {

        TokenAuthenticationProvider tokenAuthenticationProvider = new TokenAuthenticationProvider();
        tokenAuthenticationProvider.setUserDetailsService(tokenUserService());

        return tokenAuthenticationProvider;
    }

    @Bean("userDetailsService")
    @Override
    public UserDetailsService userDetailsServiceBean() throws Exception {
        List<UserDetailsService> delegateServices = new ArrayList<UserDetailsService>();
        delegateServices.add(usernamePasswordUserService());
        if (properties.getSecurity().getAuthentication().getMobile().isEnabled()) {
            delegateServices.add(mobileUserService());
        }
        if (properties.getSecurity().getAuthentication().getToken().isEnabled()) {
            delegateServices.add(tokenUserService());
        }

        return new CompositeUserService(delegateServices);
    }

    @Bean
    public UserDetailsService usernamePasswordUserService() throws Exception {
        return new UsernamePasswordUserService();
    }

    @Bean
    @ConditionalOnProperty(value = "vservice.security.authentication.mobile.enabled", havingValue = "true")
    public UserDetailsService mobileUserService() throws Exception {
        return new MobileUserService();
    }

    @Bean
    @Token
    @ConditionalOnProperty(value = "vservice.security.authentication.token.enabled", havingValue = "true")
    public UserDetailsService tokenUserService() throws Exception {
        return new TokenUserService();
    }

    @Bean
    public PasswordEncoder passwordEncoder() throws Exception {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @ConditionalOnBean(CacheManager.class)
    @ConditionalOnMissingBean
    public VerifyCodeStrategy verifyCodeStrategy(CacheManager cacheManager) throws Exception {
        DefaultVerifyCodeStrategy verifyCodeStrategy = new DefaultVerifyCodeStrategy();

        verifyCodeStrategy.setCodeType(properties.getSecurity().getVerifyCode().getType());
        verifyCodeStrategy.setCodeLength(properties.getSecurity().getVerifyCode().getLength());
        verifyCodeStrategy.setLimitTimes(properties.getSecurity().getVerifyCode().getExpiredTimes());
        verifyCodeStrategy.setCacheManager(cacheManager);

        return verifyCodeStrategy;
    }

    private AuthenticationSuccessHandler authenticationSuccessHandler() {

        SavedRequestAwareAuthenticationSuccessHandler authenticationSuccessHandler = new SavedRequestAwareAuthenticationSuccessHandler() {
            @Override
            public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {

                if (WebUtils.isRest()) {
                    response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
                    response.getWriter().write(DataUtils.toJSON(RestUtils.operate(HttpStatus.OK.value(), MessageSourceUtils.getMessage("Security.succeedLogin", "Login finished"))));
                    return;
                }

                super.onAuthenticationSuccess(request, response, authentication);
            }
        };
        authenticationSuccessHandler.setDefaultTargetUrl(properties.getWeb().getIndex());

        return authenticationSuccessHandler;
    }

    private AuthenticationFailureHandler authenticationFailureHandler() {

        SimpleUrlAuthenticationFailureHandler authenticationFailureHandler = new SimpleUrlAuthenticationFailureHandler() {
            @Override
            public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {

                if (WebUtils.isRest()) {
                    HttpStatus status = HttpStatus.UNAUTHORIZED;
                    response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
                    if (properties.getWeb().isHttpStatusFollowed()) {
                        response.setStatus(status.value());
                    }
                    response.getWriter().write(DataUtils.toJSON(RestUtils.operate(status.value(), properties.getWeb().isExceptionFollowed() ? exception.getMessage() : status.getReasonPhrase())));
                    return;
                }

                super.onAuthenticationFailure(request, response, exception);
            }
        };
        authenticationFailureHandler.setDefaultFailureUrl(properties.getWeb().getFailure());

        return authenticationFailureHandler;
    }

    private InvalidSessionStrategy invalidSessionStrategy() {
        final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

        boolean createNewSession = true;

        return (HttpServletRequest request, HttpServletResponse response) -> {
            if (createNewSession) {
                request.getSession();
            }

            if (WebUtils.isRest()) {
                response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
                if (properties.getWeb().isHttpStatusFollowed()) {
                    response.setStatus(419);
                }
                response.getWriter().write(DataUtils.toJSON(RestUtils.operate(419, MessageSourceUtils.getMessage("Security.sessionTimeout", "Session timed out, please re-login"))));
                return;
            }

            redirectStrategy.sendRedirect(request, response, properties.getWeb().getTimeout());
        };
    }

    private SessionInformationExpiredStrategy sessionInformationExpiredStrategy() {
        final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

        return (SessionInformationExpiredEvent event) -> {

            if (WebUtils.isRest()) {
                HttpServletResponse response = event.getResponse();
                response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
                if (properties.getWeb().isHttpStatusFollowed()) {
                    response.setStatus(498);
                }
                response.getWriter().write(DataUtils.toJSON(RestUtils.operate(498, MessageSourceUtils.getMessage("Security.sessionExpired", "Someone else has used your account to login"))));
                return;
            }

            redirectStrategy.sendRedirect(event.getRequest(), event.getResponse(), properties.getWeb().getExpired());
        };
    }

    @Bean
    public SessionRegistry sessionRegistry() throws Exception {
        return new SessionRegistryImpl();
    }

    @Bean
    public AbstractRememberMeServices rememberMeServices() throws Exception {
        PersistentTokenBasedRememberMeServices rememberMeServices = new PersistentTokenBasedRememberMeServices(properties.getArtifact() + "-key", userDetailsServiceBean(), sysTokenRepository);

        rememberMeServices.setParameter(properties.getSecurity().getRememberMe().getParameter());
        rememberMeServices.setCookieName(properties.getSecurity().getRememberMe().getCookie());
        rememberMeServices.setTokenValiditySeconds(properties.getSecurity().getRememberMe().getTokenValiditySeconds());

        return rememberMeServices;
    }

    private AccessDeniedHandler accessDeniedHandler() {

        return (HttpServletRequest request, HttpServletResponse response, AccessDeniedException exception) -> {

            HttpStatus status = HttpStatus.FORBIDDEN;
            if (!response.isCommitted()) {
                if (WebUtils.isRest()) {
                    response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
                    if (properties.getWeb().isHttpStatusFollowed()) {
                        response.setStatus(status.value());
                    }
                    response.getWriter().write(DataUtils.toJSON(RestUtils.operate(status.value(), properties.getWeb().isExceptionFollowed() ? exception.getMessage() : status.getReasonPhrase())));
                    return;
                }

                if (StringUtils.isNotBlank(properties.getWeb().getError())) {
                    if (properties.getWeb().isHttpStatusFollowed()) {
                        response.setStatus(status.value());
                    }
                    request.setAttribute("SPRING_SECURITY_403_EXCEPTION", exception);
                    RequestDispatcher dispatcher = request.getRequestDispatcher(properties.getWeb().getError());
                    dispatcher.forward(request, response);
                } else {
                    response.sendError(status.value(), properties.getWeb().isExceptionFollowed() ? exception.getMessage() : status.getReasonPhrase());
                }
            }
        };

    }

    private LogoutSuccessHandler logoutSuccessHandler() {
        final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

        return (HttpServletRequest request, HttpServletResponse response, Authentication authentication) -> {

            if (!response.isCommitted()) {
                if (WebUtils.isRest()) {
                    response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
                    response.getWriter().write(DataUtils.toJSON(RestUtils.operate(HttpStatus.OK.value(), MessageSourceUtils.getMessage("Security.succeedLogout", "Logout finished"))));
                    return;
                }

                redirectStrategy.sendRedirect(request, response, properties.getWeb().getLogout());
            }
        };
    }

    @Bean
    public FilterInvocationSecurityMetadataSource filterInvocationSecurityMetadataSource() throws Exception {
        return new FilterInvocationSecurityMetadataSourceImpl();
    }

    @Bean
    @DependsOn("filterInvocationSecurityMetadataSource")
    public ISecurityInitService securityInitService(SysResourceRepository sysResourceRepository) throws Exception {
        SecurityInitServiceImpl securityInitService = new SecurityInitServiceImpl();

        securityInitService.setFilterInvocationSecurityMetadataSource(filterInvocationSecurityMetadataSource());
        securityInitService.setSysResourceRepository(sysResourceRepository);

        return securityInitService;
    }

    @Bean
    @DependsOn("securityInitService")
    public FilterSecurityInterceptor filterSecurityInterceptor() throws Exception {
        FilterSecurityInterceptor filterSecurityInterceptor = new FilterSecurityInterceptor();

        List<AccessDecisionVoter<? extends Object>> decisionVoters = new ArrayList<AccessDecisionVoter<? extends Object>>();
        RoleVoter roleVoter = new RoleVoter();
        roleVoter.setRolePrefix("");
        decisionVoters.add(roleVoter);
        if (properties.getSecurity().getAdmin().isEnabled()) {
            decisionVoters.add(new AdminVoter(properties.getSecurity().getAdmin().getRole()));
        }

        filterSecurityInterceptor.setAccessDecisionManager(new AffirmativeBased(decisionVoters));
        filterSecurityInterceptor.setSecurityMetadataSource(filterInvocationSecurityMetadataSource());
        filterSecurityInterceptor.setObserveOncePerRequest(false);

        return filterSecurityInterceptor;
    }

    @Bean
    public SecurityTemplate securityTemplate(SysResourceRepository sysResourceRepository, SysRoleRepository sysRoleRepository, SysUserRepository sysUserRepository, SysGroupRepository sysGroupRepository, SysClientRepository sysClientRepository) throws Exception {
        JdbcSecurityTemplate securityTemplate = new JdbcSecurityTemplate();

        securityTemplate.setSysResourceRepository(sysResourceRepository);
        securityTemplate.setSysRoleRepository(sysRoleRepository);
        securityTemplate.setSysUserRepository(sysUserRepository);
        securityTemplate.setSysGroupRepository(sysGroupRepository);
        securityTemplate.setSysClientRepository(sysClientRepository);
        securityTemplate.setPasswordEncoder(passwordEncoder());

        return securityTemplate;
    }

    @Bean
    public AuditorAware<Long> auditorAware() throws Exception {

        return () -> {
            try {
                User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                return Optional.ofNullable(user.getId());
            } catch (ClassCastException e) {
                return Optional.empty();
            }
        };
    }
}
