package events;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import java.io.File;
import java.io.IOException;


public class replyEvents extends ListenerAdapter {

    static{
        System.setProperty("jdk.tls.maxHandshakeMessageSize", "55000");
    }

    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        MessageChannel channel = event.getChannel();
        String messageSent = event.getMessage().getContentRaw();
        //String name = event.getMember().getUser().getName();
        String[] commands = messageSent.split(" ");
        if (commands[0].equals("!dosen")) {
            scraperEvents retriever = new scraperEvents();
            int n;
            String dosenName = "";
            String dosenEmail = "";
            String imageUrl = "";
            String output = "";
            if (commands[1].contains(" ")) {
                commands[1].replace(" ", "");
            }
            n = Integer.valueOf(commands[1]);
            if (n > 0 && n < 55) {
                try {
                    String link = retriever.getLink(n);
                    //System.out.println(link);
                    if (link.isEmpty()) {
                        output = "No information";
                    } else {
                        dosenName = retriever.getDosenName(link);
                        dosenEmail = retriever.getEmail(link);
                        if (dosenEmail.isEmpty()) {
                            dosenEmail = "No email";
                        }
                        imageUrl = retriever.getImageURL(link);
                    }
                } catch (IOException io) {
                    System.out.println("Error: " + io);
                }
            } else {
                System.out.println("Command is outside the listed range");
            }
            //Sends messages through the bot
            if (!event.getMember().getUser().isBot()) {
                if (retriever.getImageFlag()) {
                    channel.sendMessage(imageUrl)
                            .queue();
                } else {
                    channel.sendMessage("No image")
                            .queue();
                }
                if (!output.equals("No information")) {
                    output = "Name: " + dosenName + '\n' + "Email: " + dosenEmail + " ";
                }
                channel.sendMessage(output)
                        /*.addFile(new File("C:/Users/user/Desktop/hotdog.png"))
                          .embed(new EmbedBuilder()
                               .setImage("attachment://hotdog.png")
                               .build())*/
                        .queue();
            }
        }
    }
}
