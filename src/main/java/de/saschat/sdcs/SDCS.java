package de.saschat.sdcs;

import com.google.inject.Inject;
import com.mrpowergamerbr.temmiewebhook.DiscordMessage;
import com.mrpowergamerbr.temmiewebhook.TemmieWebhook;
import javafx.scene.text.TextBuilder;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.*;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.config.DefaultConfig;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.entity.DestructEntityEvent;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.event.game.state.GameStoppingServerEvent;
import org.spongepowered.api.event.message.MessageChannelEvent;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.text.LiteralText;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.chat.ChatTypes;
import org.spongepowered.api.text.format.TextColors;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Plugin(
        id = "sdcs",
        name = "SDCS",
        description = "Simple Discord Chat Sync",
        authors = {
                "Sascha_T"
        }
)
public class SDCS {

    @Inject
    @DefaultConfig(sharedRoot = true)
    private Path defaultConfig;

    public String prefix;

    @Inject
    @DefaultConfig(sharedRoot = true)
    private ConfigurationLoader<CommentedConfigurationNode> configManager;

    private CommentedConfigurationNode config;

    private Logger logger;

    @Inject
    private void setLogger(Logger logger) {
        this.logger = logger;
    }

    private String token;
    private String channel;
    private JDA jda;

    TextChannel channelO;

    private int errid = 0;
    private boolean run = false;

    private Parser parser = new Parser();

    void queueMessage(Message msg) {
        if(!msg.getAuthor().getId().equals(jda.getSelfUser().getId()) && !msg.getAuthor().isFake()) {
            if (!msg.getContentRaw().equals("") || !msg.getAttachments().isEmpty() || !msg.getEmbeds().isEmpty()) {
                Text.Builder text = parser.parseDiscord(root.getNode("data", "messageformat").getString(), msg, root.getNode("data", "allowcolorsfromdiscord").getBoolean());
                text = Text.builder(sanitizeDiscord(text.build().toPlain()));
                for(Member mem : msg.getMentionedMembers()) {
                    text = Text.builder(text.build().toPlain().replace("<@"+mem.getUser().getId()+">", root.getNode("data", "messagementioncolor").getString()+"["+mem.getUser().getAsTag()+"]"+root.getNode("data", "messagetextcolor").getString()));
                }

                List<Message.Attachment> attachmentList = msg.getAttachments();
                for (Message.Attachment attach : attachmentList) {
                    try {
                        text.append(Text.builder(root.getNode("data", "attachmentformat").getString()).onClick(TextActions.openUrl(new URL(attach.getUrl()))).build()).append(Text.builder(" ").build());;
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
                for (MessageEmbed embed : msg.getEmbeds()) {
                    text.append(parser.parseEmbed(root.getNode("data", "embedformat").getString(), embed)
                                    .build()).append(Text.builder(" ").build());
                }
                Collection<Player> playerList = Sponge.getServer().getOnlinePlayers();
                for (Player p : playerList) {
                    p.sendMessage(ChatTypes.CHAT, text.build());
                }

            }
        }
    }
    public static String sanitizeDiscord(String text) {
        return text.replace("§", "&");
    }
    public static String sanitize(String text) {
        while(text.matches(".*@everyone.*")) {
            text = text.replace("@everyone", "");
        }
        while(text.matches(".*@here.*")) {
            text = text.replace("@here", "");
        }
        if(text.length() > 2000) {
            text = text.substring(0, 2000);
        }
        return text;
    }

    private int strint(String str) {
        int lon = 0xFFFFFF;
        for(char c : str.toCharArray()) {
            lon ^= c;
        }
        return lon;
    }
    private boolean highload = false;
    private Webhook getWh() {
        for (Webhook wh:
                channelO.getWebhooks().complete()) {
            if(wh.getName().equals("SDCS")) {
                return wh;
            }
        }
        return channelO.createWebhook("SDCS").complete();
    }
    private DiscordEvents d_handler = new DiscordEvents(this);
    @Listener
    public void onEntityDeath(DestructEntityEvent.Death event) {
        if(event.getTargetEntity() instanceof Player) {
            EmbedBuilder embed = new EmbedBuilder();
            embed.setAuthor(((Player) event.getTargetEntity()).getName(), null, "https://crafatar.com/avatars/" + event.getTargetEntity().getUniqueId().toString());
            embed.setColor(0xAAAA00);
            embed.setDescription(event.getMessage().toPlain());
            channelO.sendMessage(embed.build()).queue();
        }
    }
    @Listener
    public void onJoin(ClientConnectionEvent.Join event) {
        EmbedBuilder embed = new EmbedBuilder();
        embed.setAuthor(event.getTargetEntity().getName(), null, "https://crafatar.com/avatars/" + event.getTargetEntity().getUniqueId().toString());
        embed.setColor(0x00FF00);
        embed.setDescription(event.getMessage().toPlain());
        channelO.sendMessage(embed.build()).queue();
    }
    @Listener
    public void onLeave(ClientConnectionEvent.Disconnect event) {
        EmbedBuilder embed = new EmbedBuilder();
        embed.setAuthor(event.getTargetEntity().getName(), null, "https://crafatar.com/avatars/" + event.getTargetEntity().getUniqueId().toString());
        embed.setColor(0xFF0000);
        embed.setDescription(event.getMessage().toPlain());
        channelO.sendMessage(embed.build()).queue();
    }
    @Listener
    public void onChat(MessageChannelEvent.Chat e, @First Player p) {
        String unsanitized = e.getRawMessage().toPlain();
        String text = sanitize(unsanitized);
        if(!highload) {
            Webhook wh = getWh();
            TemmieWebhook tw = new TemmieWebhook(wh.getUrl());
            DiscordMessage dm = DiscordMessage.builder()
                    .avatarUrl("https://crafatar.com/avatars/"+p.getUniqueId().toString())
                    .content(text)
                    .username(p.getName())
                    .build();
                    // new DiscordMessage(p.getName(), text, "https://media.tenor.co/images/d205ef37ba5aad7b84fc21f6ffb36c6b/raw")); // "https://crafatar.com/avatars/"+p.getUniqueId()));
            tw.sendMessage(dm);
        } else {
            EmbedBuilder embed = new EmbedBuilder();
            embed.setAuthor(p.getName(), null, "https://crafatar.com/avatars/"+p.getUniqueId().toString());
            embed.setColor(new Random(strint(p.getName())).nextInt(0xFFFFFF));
            embed.setDescription(text);
            MessageEmbed emb = embed.build();
            channelO.sendMessage(emb).queue();
        }
    }
    @Listener
    public void onServerStop(GameStoppingServerEvent event) {
        EmbedBuilder embed = new EmbedBuilder();
        embed.setAuthor("Server");
        embed.setColor(0xFF0000);
        embed.setDescription("Server stopping!");
        channelO.sendMessage(embed.build()).queue();
    }
    ConfigurationNode root = null;
    @Listener
    public void onServerStart(GameStartedServerEvent event) {
        logger.info("Initialising SDCS");
        if (!Files.exists(defaultConfig)) {
            try {
                if(Sponge.getAssetManager().getAsset(this, "default.conf").isPresent())
                    Sponge.getAssetManager().getAsset(this, "default.conf").get().copyToFile(defaultConfig);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        try {
            root  = configManager.load();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        token = root.getNode("data", "token").getString();
        channel = root.getNode("data", "channel").getString();
        highload = root.getNode("data", "highload").getBoolean();
        try {
            jda = new JDABuilder(token).addEventListener(d_handler).addEventListener(new ListenerAdapter() {
            @Override public void onReady(ReadyEvent event) {
                        logger.info("Using channel {}", channel);
                        channelO = jda.getTextChannelById(channel);
                        if(channelO != null) {
                                run = true;
                        } else {
                                errid = 2;
                        }
            }
        }).build();
        } catch(Exception ex) {
            ex.printStackTrace();
            run = false;
            errid = 1;
        }
        if(!run) {
            logger.error("Won't run. (Error {})", errid);
        } else {
            logger.info("Now running.");
        }
        EmbedBuilder embed = new EmbedBuilder();
        embed.setAuthor("Server");
        embed.setColor(0x00FF00);
        embed.setDescription("Server started!");
        prefix = root.getNode("data", "prefix").getString();
        channelO.sendMessage(embed.build()).queue();
    }
}
