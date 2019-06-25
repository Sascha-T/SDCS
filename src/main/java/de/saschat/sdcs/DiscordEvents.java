package de.saschat.sdcs;

import net.dv8tion.jda.core.events.Event;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.EventListener;

public class DiscordEvents implements EventListener {
    private SDCS parent;
    public DiscordEvents(SDCS parent) {
        this.parent = parent;
    }
    @Override
    public void onEvent(Event event) {
        if(event instanceof MessageReceivedEvent)
            if(((MessageReceivedEvent) event).getChannel() == parent.channelO)
                parent.queueMessage(((MessageReceivedEvent) event).getMessage());
    }
}
