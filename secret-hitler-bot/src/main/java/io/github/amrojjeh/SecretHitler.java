package io.github.amrojjeh;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.requests.restaction.GuildAction;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class SecretHitler extends ListenerAdapter {
	public final JDA jda;
	public final Lobby lobby;
	public Guild gameGuild;
	
	private boolean _is_registered;

	public void registerAsListener() {
		if (_is_registered) {
			jda.removeEventListener(this);
			_is_registered = false;
		} else {
			jda.addEventListener(this);
			_is_registered = true;
		}
	}

	private SecretHitler(JDA jda, Lobby lobby) {
		this.jda = jda;
		this.lobby = lobby;
		_is_registered = false;
		gameGuild = null;
	}

	public static SecretHitler createServer(JDA jda, Lobby lobby) {
		if (jda.getGuilds().size() >= 10) return null;
		GuildAction g = jda.createGuild("Secret Hitler");
		g.newRole().setName(lobby.id.toString()).setPosition(0);
		g.newChannel(ChannelType.TEXT, "general");
		g.queue();
		SecretHitler h = new SecretHitler(jda, lobby);
		h.registerAsListener();
		return h;
	}

	@Override
	public void onGuildJoin(GuildJoinEvent e) {
		if (e.getGuild().getRoles().get(0).getName().equals(lobby.id.toString())) {
			gameGuild = e.getGuild();
			gameGuild.getChannels().get(0).createInvite().queue(invite -> lobby.game_host.getUser().openPrivateChannel().queue(pm -> pm.sendMessage(invite.getUrl()).queue()));			
		}
	}
}
