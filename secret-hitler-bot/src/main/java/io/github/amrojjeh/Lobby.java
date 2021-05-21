package io.github.amrojjeh;

import java.util.UUID;

import net.dv8tion.jda.api.entities.Member;

public class Lobby {
	public final UUID id;
	public final Member game_host;

	public Lobby(Member host) {
		this.id = UUID.randomUUID();
		this.game_host = host;
	}
}
