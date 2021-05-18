package io.github.amrojjeh;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import javax.security.auth.login.LoginException;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.entities.ChannelType;

/**
 * Hello world!
 *
 */
public class Listener extends ListenerAdapter
{
    public static void main( String[] args ) throws LoginException
    {
        String token = loadToken();
        if (token == null)
        {
            System.out.println("Could not load token. Make sure to place your token in token.txt.");
            return;
        }

        JDA jda = JDABuilder.createDefault(token).build();
        jda.addEventListener(new Listener());
    }

    public static String loadToken()
    {
        File file = new File("token.txt");
        Scanner scanner;
        try {
            scanner = new Scanner(file);
        } catch (FileNotFoundException e) {
            return null;
        }
        if (!scanner.hasNextLine())
        {
            scanner.close();
            return null;
        }
        String token = scanner.nextLine();
        scanner.close();
        return token;
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event)
    {
        if (event.isFromType(ChannelType.PRIVATE))
        {
            System.out.printf("[PM] %s: %s\n", event.getAuthor().getName(),
                                    event.getMessage().getContentDisplay());
        }
        else
        {
            System.out.printf("[%s][%s] %s: %s\n", event.getGuild().getName(),
                        event.getTextChannel().getName(), event.getMember().getEffectiveName(),
                        event.getMessage().getContentDisplay());
        }
    }
}
