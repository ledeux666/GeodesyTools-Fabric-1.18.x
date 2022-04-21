package ru.ledeux.geotools;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import ru.ledeux.geotools.block.*;
import ru.ledeux.geotools.block.entity.BoxBlockEntity;
import ru.ledeux.geotools.block.entity.DemoBlockEntity;
import ru.ledeux.geotools.item.Ruby;
import ru.ledeux.geotools.screen.BoxScreenHandler;

// Главный класс мода (входная точка).
public class GeoTools implements ModInitializer {

    // Идентификатор мода и его имя.
    public static final String MOD_ID = "geotools";
    public static final String MOD_NAME = "Geodesy Tools";

    // Создание креативной вкладки для предметов мода.
    // TODO: сделать иконку тахеометра.
    public static final ItemGroup GEO_TOOLS = FabricItemGroupBuilder.build(
            new Identifier(MOD_ID, "general"),
            () -> new ItemStack(Blocks.AMETHYST_CLUSTER));

    // Создание предметов, блоков и сущностей.
    // Параметр сборки BlockEntity в конце строки должен быть "null".
    public static final Ruby RUBY = new Ruby(new FabricItemSettings().group(GeoTools.GEO_TOOLS).maxCount(64));

    public static final Block TOTAL_STATION =
            new TotalStation(FabricBlockSettings.of(Material.METAL).strength(4.0f).nonOpaque());
    public static final Item TOTAL_STATION_BI =
            new BlockItem(TOTAL_STATION, new FabricItemSettings().group(GeoTools.GEO_TOOLS).maxCount(64));

    public static final Block REFLECTOR =
            new Reflector(FabricBlockSettings.of(Material.METAL).strength(4.0f).nonOpaque());
    public static final Item REFLECTOR_BI =
            new BlockItem(REFLECTOR, new FabricItemSettings().group(GeoTools.GEO_TOOLS).maxCount(64));

    public static final Block FORCED_CENTERING_PYLON =
            new ForcedCenteringPylon(FabricBlockSettings.of(Material.METAL).strength(4.0f).nonOpaque());
    public static final Item FORCED_CENTERING_PYLON_BI =
            new BlockItem(FORCED_CENTERING_PYLON, new FabricItemSettings().group(GeoTools.GEO_TOOLS).maxCount(64));

    public static final Block VERTICAL_SLAB_BLOCK =
            new VerticalSlabBlock(FabricBlockSettings.of(Material.STONE).strength(4.0f).nonOpaque());
    public static final Item VERTICAL_SLAB_BLOCK_BI =
            new BlockItem(VERTICAL_SLAB_BLOCK, new FabricItemSettings().group(GeoTools.GEO_TOOLS).maxCount(64));

    public static final Block STATE_TEST =
            new StateTest(FabricBlockSettings.of(Material.METAL).strength(4.0f).nonOpaque());
    public static final Item STATE_TEST_BI =
            new BlockItem(STATE_TEST, new FabricItemSettings().group(GeoTools.GEO_TOOLS).maxCount(64));

    public static final Block DEMO_BLOCK =
            new DemoBlock(FabricBlockSettings.of(Material.METAL).strength(4.0f).nonOpaque());
    public static final Item DEMO_BLOCK_BI =
            new BlockItem(DEMO_BLOCK, new FabricItemSettings().group(GeoTools.GEO_TOOLS).maxCount(64));
    public static final BlockEntityType<DemoBlockEntity> DEMO_BLOCK_ENTITY =
            FabricBlockEntityTypeBuilder.create(DemoBlockEntity::new, DEMO_BLOCK).build(null);

    public static final Block BOX_BLOCK =
            new BoxBlock(FabricBlockSettings.of(Material.METAL).strength(4.0f).nonOpaque());
    public static final Item BOX_BLOCK_BI =
            new BlockItem(BOX_BLOCK, new FabricItemSettings().group(GeoTools.GEO_TOOLS).maxCount(64));
    public static final BlockEntityType<BoxBlockEntity> BOX_BLOCK_ENTITY =
            FabricBlockEntityTypeBuilder.create(BoxBlockEntity::new, BOX_BLOCK).build(null);

    // Регистрация обработчка экрана.
    // TODO: что-то здесь не так
    public static final ScreenHandlerType<BoxScreenHandler> BOX_SCREEN_HANDLER =
            ScreenHandlerRegistry.registerExtended(id("box"), BoxScreenHandler::new);

    // Метод для быстрого создания идентификатора.
    public static Identifier id(String path) {
        return new Identifier(MOD_ID, path);
    }

    // Наследованный метод инициализации зарегистрированных предметов, блоков, сущностей и т.д.
    @Override
    public void onInitialize() {

        Registry.register(Registry.ITEM, id("ruby"), RUBY);

        Registry.register(Registry.BLOCK, id("total_station"), TOTAL_STATION);
        Registry.register(Registry.ITEM, id("total_station"), TOTAL_STATION_BI);

        Registry.register(Registry.BLOCK, id("reflector"), REFLECTOR);
        Registry.register(Registry.ITEM, id("reflector"), REFLECTOR_BI);

        Registry.register(Registry.BLOCK, id("forced_centering_pylon"), FORCED_CENTERING_PYLON);
        Registry.register(Registry.ITEM, id("forced_centering_pylon"), FORCED_CENTERING_PYLON_BI);

        Registry.register(Registry.BLOCK, id("vertical_slab_block"), VERTICAL_SLAB_BLOCK);
        Registry.register(Registry.ITEM, id("vertical_slab_block"), VERTICAL_SLAB_BLOCK_BI);

        Registry.register(Registry.BLOCK, id("state_test"), STATE_TEST);
        Registry.register(Registry.ITEM, id("state_test"), STATE_TEST_BI);

        Registry.register(Registry.BLOCK, id("demo_block"), DEMO_BLOCK);
        Registry.register(Registry.ITEM, id("demo_block"), DEMO_BLOCK_BI);
        Registry.register(Registry.BLOCK_ENTITY_TYPE, id("demo_block_entity"), DEMO_BLOCK_ENTITY);

        Registry.register(Registry.BLOCK, id("box_block"), BOX_BLOCK);
        Registry.register(Registry.ITEM, id("box_block"), BOX_BLOCK_BI);
        Registry.register(Registry.BLOCK_ENTITY_TYPE, id("box_block_entity"), BOX_BLOCK_ENTITY);
    }
}
