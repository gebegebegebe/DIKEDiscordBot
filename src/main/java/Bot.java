import events.replyEvents;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

public class Bot{

    static{
        System.setProperty("jdk.tls.maxHandshakeMessageSize", "55000");
    }

    public static void main(String args[]) throws Exception{
        JDA jda = JDABuilder.createDefault("<Insert Bot token here>").build();
        jda.addEventListener(new replyEvents());
    }
}
