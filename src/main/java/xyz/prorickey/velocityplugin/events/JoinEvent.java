package xyz.prorickey.velocityplugin.events;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.PostLoginEvent;
import xyz.prorickey.velocityplugin.VelocityPlugin;

public class JoinEvent {

    @Subscribe
    public void onPostLogin(PostLoginEvent event) {
        // VelocityPlugin.getDatabase().dbLoginLogic(event.getPlayer());
    }

}
