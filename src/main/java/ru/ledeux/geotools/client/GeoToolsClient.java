package ru.ledeux.geotools.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import ru.ledeux.geotools.GeoTools;
import ru.ledeux.geotools.client.BoxScreen;

// Клиентская часть мода
@Environment(EnvType.CLIENT)
public class GeoToolsClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        // Регистрация клиентского экрана
        ScreenRegistry.register(GeoTools.BOX_SCREEN_HANDLER, BoxScreen::new);
    }
}
