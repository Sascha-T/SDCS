# SIMPLE DISCORD CHAT SYNC (SDCS)

### Synchronise your ingame chat with discord!


## Configuration

config/sdcs.conf

```
data {
    token="TOKEN HERE"
    channel="CHANNELID HERE"
    highload=false

    messageformat="§0[§1DISCORD§0]§r %t§7:§f %m§r"
    messagetextcolor="§r"
    messagementioncolor="§1"

    attachmentformat="§9[Attachment]§r"
    embedformat="§9[Embed: %d]§r"

    allowcolorsfromdiscord=true
}
```

Token is your discord bots token, \
it is required for server messages, \
but automatic webhook management is also included!

The highload function was added when I thought \
that some people may prefer a simpler, less \
CPU heavy alternative for bigger servers, \
then I realised its pointless since using this \
on bigger servers would get you ratelimited really quickly.

But hey, if you like the style of it, use it!

Explanation: ![Well rip](https://raw.githubusercontent.com/Sascha-T/SDCS/master/DiscordCanary_2019-06-25_18-26-09.png "I am certainly veryyy smart.")

Formats can use all minecraft colors (prefixed with §), \
and can use variables. 

Variable list: \

``messageformat``: \
    %t = Tag of user \
    %u = Username \
    %d = Discriminator (#NUMBER) \
    %m = Content
    
``embedformat``
    %d = Description (WARN: May be null)


#### Allow colors from discord
This is a small command which regulates whether Discord users can put minecraft colors in their messages. \
Incase people are discussing legal matters it will be replaced with &,
which should prevent removing context.

## Compilation

Linux: 
``./gradlew shadowJar`` \
Windows: 
``gradlew shadowJar``

Output is in build/libs/ \
Use the `-all` jar file.

## License
GPL v3.0
