# SpringSecurityDemo
SpringSecurity Demo


########################  Spring Security（基于版本spring-security-core-5.1.5.release）原理研究  #############################
核心组件：
1、SecurityContextHolder：用来操作SecurityContext，SecurityContext存储Authentication的容器；再将SecurityContext存到session里面
2、Authentication对象：对用户信息的一个简单封装，用于存储身份验证信息
3、UserDetails：只是用于存储用户信息并最终封装到 Authentication 对象中。
4、接口UserDetailsService：只定义了一个只读方法，返回一个 UserDetails 接口对象。自定义情况下，实现该类重写loadUserByUsername()方法
5、接口AuthenticationManager：
6、AuthenticationProvider：UserDetailsService 可以作为 AuthenticationProvider 的一个属性，在 AuthenticationProvider 执行请求认证时调用 UserDetailsService 的 loadUserByUsername 方法返回一个 UserDetails 对象，并使用此 UserDetails 对象封装最终的 Authentication 对象



总结流程：
web-->Filter链（各种filter实现AbstractAuthenticationProcessingFilter抽象父类 ）-->Manager（接口AuthenticationManager）-->Provider（DaoAuthenticationProvider继承抽象父类AbstractUserDetailsAuthenticationProvider，父类实现接口：AuthenticationProvider ）-->UserDetailService(实现这个接口)

返回来：AuthenticationProvider --(返回Authentication 对象)-->ProviderManager 返回对象 -->AbstractAuthenticationProcessingFilter 返回-->存入SecurityContextHolder

一、Filters初始化流程
二、登录流程
三、权限校验流程


AbstractAuthenticationProcessingFilter
AuthenticationManager
AuthenticationProvider


--------------------- 验证过程
1.用户编写WebSecurityConfigurerApdater的继承类，配置HttpSecurity，包括formLogin，antMatcher，hasRole等等。

2.项目启动自动装配FilterChain，访问不同uri对应不同的Filter Chain。

3.用户输入账号、密码点击登录，FilterChainProxy中的UsernamePasswordAuthenticationFilter获取request中的用户名、密码，验证身份信息

4.doFilter()过程中会执行ProviderManager.authenticate()，即遍历所有AuthenticationProvider执行authenticate()方法。

5.authenticate()方法中会调用userDetailService，用户自定义类继承UserDetailService，并重写其中的方法loadUserByUsername()，从数据库中获取用户信息进行比对

6.比对成功后将用户信息和角色信息整合成Authentication，并存入SecurityContext中，同时将SecurityContext也存入session中，跳转到主页面。
7.比对失败，SecurityContext中没有Authentication，FilterChain进行到最后一步FilterSecurityInterceptor，判断用户角色是否能访问request中的访问地址即资源。如果

不行则报错跳转到指定页面；如果成功则进入request调用的资源。

8.注销操作由LogoutFilter执行，执行session.invalidate()和SecurityContextHolder.clearContext()。

--------------------- 


==实现步骤：
1.配置类WebSecurityConfig继承WebSecurityConfigurerAdapter（配合@EnableWebSecurity）
2.重载3个方法：
 2.1重载configure(HttpSecurity)方法，通过重载，配置如何通过拦截器保护请求
 2.2重载configure(AuthenticationManagerBuilder)方法，通过重载，配置user-detail服务
 2.3重载configure(WebSecurity)方法，通过重载，配置Security的Filter链
