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
