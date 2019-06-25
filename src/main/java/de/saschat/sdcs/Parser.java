package de.saschat.sdcs;

import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageEmbed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.api.text.LiteralText;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColor;
import org.spongepowered.api.text.format.TextColors;

import java.util.HashMap;
public class Parser {
    public Text.Builder parseDiscord(String text, Message msg, boolean sanitize) {
        text = text.replace("%t", msg.getAuthor().getAsTag());
        text = text.replace("%u", msg.getAuthor().getName());
        text = text.replace("%d", msg.getAuthor().getDiscriminator());
        if(sanitize) {
            text = text.replace("%m", SDCS.sanitizeDiscord(msg.getContentRaw()));
        } else {
            text = text.replace("%m" ,msg.getContentRaw());
        } // allowcolorsfromdiscord
        return Text.builder(text);
    }

    public Text.Builder parseEmbed(String text, MessageEmbed embed) {
        text = text.replace("%d", embed.getDescription());
        return Text.builder(text);
    }
}
