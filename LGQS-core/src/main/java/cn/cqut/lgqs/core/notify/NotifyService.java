package cn.cqut.lgqs.core.notify;

import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.scheduling.annotation.Async;

/**
 * 餐厅通知服务类
 */
public class NotifyService {
    private MailSender mailSender;
    private String sendFrom;
    private String sendTo;

    private SmsSender smsSender;

    public boolean isSmsEnable() {
        return smsSender != null;
    }

    /**
     * 邮件消息通知,
     * 接收者在spring.mail.sendto中指定
     *
     * @param subject 邮件标题
     * @param content 邮件内容
     */
    @Async
    public void notifyMail(String subject, String content) {
        if (mailSender == null) {
            return;
        }

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(sendFrom + "(理工轻食)");
        message.setTo(sendTo);
        message.setSubject(subject);
        message.setText(content);
        mailSender.send(message);
    }

    public void setMailSender(MailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void setSendFrom(String sendFrom) {
        this.sendFrom = sendFrom;
    }

    public void setSendTo(String sendTo) {
        this.sendTo = sendTo;
    }

    public void setSmsSender(SmsSender smsSender) {
        this.smsSender = smsSender;
    }
}
