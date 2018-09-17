package Michal.appdemo.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    //Kodowanie i rozkodowanie haseł
    private BCryptPasswordEncoder bcp;

    @Autowired
    //Dostęp do bazy danych
    private DataSource ds;

    @Value("${spring.queries.users-query}")
    //pobieranie user i pass
    private String usersQuery;

    @Value("${spring.queries.roles-query}")
    //role dla usera
    private String rolesQuery;

    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication().usersByUsernameQuery(usersQuery)
                .authoritiesByUsernameQuery(rolesQuery)
                .dataSource(ds).passwordEncoder(bcp);
    }

    protected void configure(HttpSecurity httpSec) throws Exception {
        httpSec
                .authorizeRequests()
                .antMatchers("/").permitAll()
                .antMatchers("/index").permitAll()
                .antMatchers("/login").permitAll()
                .antMatchers("/register").permitAll()
                .antMatchers("/adduser").permitAll()
                .antMatchers("/admin").hasAuthority("ROLE_ADMIN")
                .anyRequest().authenticated()
                .and().csrf().disable()
                .formLogin()
                .loginPage("/login")
                .failureUrl("/login?error=true")
                .defaultSuccessUrl("/").usernameParameter("email")
                .passwordParameter("password")
                .and().logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/")
                .and().exceptionHandling().accessDeniedPage("/denied");
    }

    public void configure(WebSecurity webSec) throws Exception {
        webSec.ignoring()
                .antMatchers("/resources/**", "/statics/**", "/css/**", "/js/**", "/images/**", "/incl/**");
    }

}