package com.hncs.world.common.utils;

import com.hncs.world.common.ErrorCode;
import com.hncs.world.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Component;

import javax.net.ssl.*;
import java.security.cert.X509Certificate;
import java.util.Properties;

import static com.hncs.world.common.ErrorCode.SERVER_ERROR;

/**
 * 邮箱工具类
 * 负责发送验证码邮件
 */
@Component
public class EmailUtil {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    /**
     * 发送验证码邮件
     * @param toEmail 接收邮箱
     * @param verificationCode 验证码
     * @throws BusinessException 邮件发送失败时抛出业务异常
     */
    public void sendVerificationCode(String toEmail, String verificationCode) {
        try {
            // 关键：给 JavaMailSender 配置自定义 SSL 工厂，跳过证书验证
            configureJavaMailSsl((JavaMailSenderImpl) mailSender);

            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(toEmail);
            message.setSubject("验证码 - 您的账户安全验证");
            message.setText(buildEmailContent(verificationCode));

            mailSender.send(message);
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.SERVER_ERROR,"邮件发送失败");
        }
    }

    /**
     * 构建邮件内容
     */
    private String buildEmailContent(String code) {
        return "亲爱的用户：\n\n" +
                "您的验证码是：" + code + "\n\n" +
                "该验证码5分钟内有效，请勿泄露给他人。\n\n" +
                "如果不是您本人操作，请忽略此邮件。\n\n" +
                "系统邮件，请勿回复";
    }

    /**
     * 核心方法：给 JavaMailSender 配置自定义 SSL 工厂，跳过证书验证
     */
    private void configureJavaMailSsl(JavaMailSenderImpl mailSender) throws Exception {
        // 1. 创建信任所有证书的 SSLSocketFactory
        SSLSocketFactory sslSocketFactory = createTrustAllSslSocketFactory();

        // 2. 获取 JavaMail 的配置属性
        Properties props = mailSender.getJavaMailProperties();

        // 3. 强制 JavaMail 使用自定义 SSL 工厂（关键配置）
        props.put("mail.smtp.ssl.socketFactory", sslSocketFactory);
        // 关闭主机名验证（避免证书域名不匹配报错）
        props.put("mail.smtp.ssl.trust", "*"); // 信任所有主机
        props.put("mail.smtp.ssl.checkserveridentity", "false"); // 不检查主机身份
    }

    /**
     * 创建信任所有证书的 SSLSocketFactory
     */
    private SSLSocketFactory createTrustAllSslSocketFactory() throws Exception {
        // 信任所有证书的 TrustManager
        TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    @Override
                    public X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }

                    @Override
                    public void checkClientTrusted(X509Certificate[] certs, String authType) {
                    }

                    @Override
                    public void checkServerTrusted(X509Certificate[] certs, String authType) {
                    }
                }
        };

        // 初始化 SSL 上下文
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, trustAllCerts, new java.security.SecureRandom());

        return sslContext.getSocketFactory();
    }
}