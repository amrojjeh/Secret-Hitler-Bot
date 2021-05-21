package io.github.amrojjeh;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import javax.security.auth.login.LoginException;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.entities.Guild;

public class MainListener extends ListenerAdapter {
    public final JDA jda;

    public MainListener(JDA jda) {
        this.jda = jda;
    }

    public static void main( String[] args ) throws LoginException {
        String token = loadToken();
        if (token == null) {
            System.out.println("Could not load token. Make sure to place your token in token.txt.");
            return;
        }

        JDA jda = JDABuilder.createDefault(token).build();
        jda.addEventListener(new MainListener(jda));
    }

    public static String loadToken() {
        try {
            List<String> lines = Files.readAllLines(Paths.get("token.txt"));
            if (lines.size() == 0) return null;
            return lines.get(0);
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    public void onReady(ReadyEvent e) {
        System.out.printf("We are in %d available servers!\n", e.getGuildAvailableCount());
        int count = 0;
        for (Guild g : jda.getGuilds()) {
            if (g.getOwner().getUser().getIdLong() == jda.getSelfUser().getIdLong()) {
                ++count;
                g.delete().queue();
            }
        }
        System.out.printf("We deleted %d servers, so we are now in %d servers\n", count, e.getGuildAvailableCount() - count);
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        String msg = event.getMessage().getContentStripped();
        if (msg.equalsIgnoreCase("sh.start")) {
            Lobby l = new Lobby(event.getMember());
            SecretHitler h = SecretHitler.createServer(jda, l);
            if (h == null) event.getMessage().reply("Server could not be created since we are in more than 10 guilds!").queue();
            else event.getMessage().reply("Server created!").queue();
        }
    }
}
