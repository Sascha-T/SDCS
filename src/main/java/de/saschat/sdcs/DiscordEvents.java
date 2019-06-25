package de.saschat.sdcs;

import net.dv8tion.jda.core.events.Event;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.EventListener;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;

import java.util.Collection;

public class DiscordEvents implements EventListener {
    private SDCS parent;

    public DiscordEvents(SDCS parent) {
        this.parent = parent;
    }

    @Override
    public void onEvent(Event event) {
        if (event instanceof MessageReceivedEvent) {
            MessageReceivedEvent ev = (MessageReceivedEvent) event;
            if (ev.getChannel() == parent.channelO)
                parent.queueMessage(ev.getMessage());
        }
    }
}
