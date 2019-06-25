# SIMPLE DISCORD CHAT SYNC (SDCS)

### Synchronise your ingame chat with discord!


## Configuration

config/sdcs.conf

```
data {
    token="Token" 
    channel="Channel ID"
    highload=false
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

## Compilation

Linux: 
``./gradlew shadowJar`` \
Windows: 
``gradlew shadowJar``

Output is in build/libs/ \
Use the `-all` jar file.

## License
GPL v3.0
