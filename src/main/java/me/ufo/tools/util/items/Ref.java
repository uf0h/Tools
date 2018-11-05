package me.ufo.tools.util.items;

import me.ufo.tools.util.Style;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.function.BiConsumer;

public class Ref {

    public static final String VERSION = org.bukkit.Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3];

    public static final Class<?> CraftPlayer = getOBC("entity.CraftPlayer"), IChatBaseComponent = getNMS("IChatBaseComponent"),
            ChatSerializer = getNMS("IChatBaseComponent$ChatSerializer"),
            PacketPlayOutChat = getNMS("PacketPlayOutChat"),
            EntityPlayer = getNMS("EntityPlayer"),
            PlayerConnection = getNMS("PlayerConnection"),
            Packet = getNMS("Packet");

    public static final Method CRAFT_PLAYER_GET_HANDLE = getMethod(CraftPlayer, "getHandle"),
            CHAT_SERIALIZER_A = getMethod(ChatSerializer, "a", String.class),
            PLAYER_CONNECTION_SEND_PACKET = getMethod(PlayerConnection, "sendPacket", Packet);

    public static final Field ENTITY_PLAYER_PLAYER_CONNECTION = getField(EntityPlayer, "playerConnection");

    private static BiConsumer<Player, String> actionBarSender = null;

    static {
        if (VERSION.contains("12")) {
            init12ActionBar();
        } else {
            initPre12ActionBar();
        }
    }

    private static void init12ActionBar() {
        final Class<?> ChatMessageType = getNMS("ChatMessageType");
        final Object[] enumConstants = ChatMessageType.getEnumConstants();
        Object gameInfo = null;
        for (Object enumConstant : enumConstants) {
            if (enumConstant.toString().equals("GAME_INFO")) {
                gameInfo = enumConstant;
                break;
            }
        }
        final Constructor<?> PacketPlayOutChatConstructor = getConstructor(PacketPlayOutChat, IChatBaseComponent, ChatMessageType);
        Object finalGameInfo = gameInfo;
        actionBarSender = (p, message) -> {
            try {
                Object handle = CRAFT_PLAYER_GET_HANDLE.invoke(p);
                Object playerConnection = ENTITY_PLAYER_PLAYER_CONNECTION.get(handle);
                Object baseComponent = CHAT_SERIALIZER_A.invoke(null, "{\"text\": \"" + message + "\"}");
                Object packet = PacketPlayOutChatConstructor.newInstance(baseComponent, finalGameInfo);
                PLAYER_CONNECTION_SEND_PACKET.invoke(playerConnection, packet);
            } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
                e.printStackTrace();
            }
        };
    }

    private static void initPre12ActionBar() {
        final Constructor<?> PacketPlayOutChatConstructor = getConstructor(PacketPlayOutChat, IChatBaseComponent, Byte.TYPE);
        actionBarSender = (p, message) -> {
            try {
                Object handle = CRAFT_PLAYER_GET_HANDLE.invoke(p);
                Object playerConnection = ENTITY_PLAYER_PLAYER_CONNECTION.get(handle);
                Object baseComponent = CHAT_SERIALIZER_A.invoke(null, "{\"text\": \"" + message + "\"}");
                Object packet = PacketPlayOutChatConstructor.newInstance(baseComponent, (byte) 2);
                PLAYER_CONNECTION_SEND_PACKET.invoke(playerConnection, packet);
            } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
                e.printStackTrace();
            }
        };
    }

    public static void sendActionBar(Player toPlayer, String withMessage) {
        actionBarSender.accept(toPlayer, Style.translate(withMessage));
    }

    public static Class<?> getNMS(String classPath) {
        try {
            Class<?> clazz = Class.forName("net.minecraft.server." + VERSION + "." + classPath);
            return clazz;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Class<?> getOBC(String classPath) {
        try {
            Class<?> clazz = Class.forName("org.bukkit.craftbukkit." + VERSION + "." + classPath);
            return clazz;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Method getMethod(Class<?> clazz, String methodName, Class<?>... params) {
        while (clazz != null) {
            Method[] methods = clazz.getDeclaredMethods();
            for (Method method : methods) {
                if (method.getName().equals(methodName) && compare(method.getParameterTypes(), params)) {
                    method.setAccessible(true);
                    return method;
                }
            }
            clazz = clazz.getSuperclass();
        }
        return null;
    }

    public static Constructor<?> getConstructor(Class<?> clazz, Class<?>... params) {
        while (clazz != null) {
            Constructor<?>[] constructors = clazz.getDeclaredConstructors();
            for (Constructor<?> cons : constructors) {
                if (compare(cons.getParameterTypes(), params)) {
                    return cons;
                }
            }
            clazz = clazz.getSuperclass();
        }
        return null;
    }

    public static Method getMethod(Object object, String methodName, Class<?>... params) {
        return getMethod(object.getClass(), methodName, params);
    }

    public static Field getField(Class<?> clazz, String fieldName) {
        while (clazz != null) {
            Field[] fields = clazz.getDeclaredFields();
            for (Field f : fields) {
                if (f.getName().equals(fieldName)) {
                    f.setAccessible(true);
                    return f;
                }
            }
            clazz = clazz.getSuperclass();
        }
        return null;
    }

    public static boolean compare(Class<?>[] classes, Class<?>[] otherClasses) {
        if (classes == null || otherClasses == null)
            return false;
        if (classes.length != otherClasses.length)
            return false;
        for (int i = 0; i < classes.length; i++) {
            if (classes[i] != otherClasses[i])
                return false;
        }

        return true;
    }

    private static Class<?>[] params(Class<?>... classes) {
        return classes;
    }

    public static class NBT {

        public static final Class<?>
                CLASS_NBT_TAG_BASE = getNMS("NBTBase"),
                CLASS_NBT_TAG_COMPOUND = getNMS("NBTTagCompound"),
                CLASS_NBT_LIST = getNMS("NBTTagList"),
                CLASS_INTEGER_ARRAY = new int[]{}.getClass(),
                CLASS_BYTE_ARRAY = new int[]{}.getClass(),
                CLASS_CRAFT_ITEM_STACK = getOBC("inventory.CraftItemStack"),
                CLASS_NMS_ITEM_STACK = getNMS("ItemStack");

        private final static Method AS_NMY_COPY_METHOD = getMethod(CLASS_CRAFT_ITEM_STACK, "asNMSCopy", ItemStack.class);
        private static final Method AS_BUKKIT_COPY_METHOD = getMethod(CLASS_CRAFT_ITEM_STACK, "asBukkitCopy", CLASS_NMS_ITEM_STACK);
        private static final Method GET_TAG_METHOD = getMethod(CLASS_NMS_ITEM_STACK, "getTag");
        private static final Method SET_TAG_METHOD = getMethod(CLASS_NMS_ITEM_STACK, "setTag", CLASS_NBT_TAG_COMPOUND);
        private static final Method HAS_KEY_METHOD = getMethod(CLASS_NBT_TAG_COMPOUND, "hasKey", String.class);
        private static final Method SET_STRING_METHOD = getMethod(CLASS_NBT_TAG_COMPOUND, "setString", params(String.class, String.class));
        private static final Method SET_INT_METHOD = getMethod(CLASS_NBT_TAG_COMPOUND, "setInt", String.class, Integer.TYPE);
        private static final Method SET_BOOLEAN_METHOD = getMethod(CLASS_NBT_TAG_COMPOUND, "setBoolean", String.class, Boolean.TYPE);
        private static final Method SET_DOUBLE_METHOD = getMethod(CLASS_NBT_TAG_COMPOUND, "setDouble", String.class, Double.TYPE);
        private static final Method SET_LONG_METHOD = getMethod(CLASS_NBT_TAG_COMPOUND, "setLong", String.class, Long.TYPE);
        private static final Method GET_DOUBLE_METHOD = getMethod(CLASS_NBT_TAG_COMPOUND, "getDouble", String.class);
        private static final Method GET_INT_METHOD = getMethod(CLASS_NBT_TAG_COMPOUND, "getInt", String.class);
        private static final Method GET_LONG_METHOD = getMethod(CLASS_NBT_TAG_COMPOUND, "getLong", String.class);
        private static final Method GET_STRING_METHOD = getMethod(CLASS_NBT_TAG_COMPOUND, "getString", String.class);
        private static final Method GET_COMPOUND_METHOD = getMethod(CLASS_NBT_TAG_COMPOUND, "getCompound", String.class);
        Constructor<?> NBT_TAG_COMPOUND_CONSTRUCTOR = getConstructor(CLASS_NBT_TAG_COMPOUND);

        public static Object toNMSItem(ItemStack item) {
            if (item == null)
                return null;
            try {
                return AS_NMY_COPY_METHOD.invoke(null, item);
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                e.printStackTrace();
            }
            return null;
        }

        public static ItemStack toBukkitItem(Object obcItem) {
            if (obcItem == null)
                return null;
            try {
                return (ItemStack) AS_BUKKIT_COPY_METHOD.invoke(null, obcItem);
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                e.printStackTrace();
            }
            return null;
//			ItemStack it = CraftItemStack.asBukkitCopy(null);
        }

        public static Object getNBTTag(Object nmsItem) {
            try {
                Object nbt = GET_TAG_METHOD.invoke(nmsItem);
                if (nbt == null) {
//					System.out.println("it was null");
                    nbt = createNBTTag();
                }
                return nbt;
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                e.printStackTrace();
            }
            return null;
        }

        public static Object createNBTTag() {
            try {
//				return CLASS_NBT_TAG_COMPOUND.newInstance();
                return CLASS_NBT_TAG_COMPOUND.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
            return null;
        }

        public static void applyNBT(Object nmsItem, Object nbt) {
            try {
                SET_TAG_METHOD.invoke(nmsItem, nbt);
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }

        public static boolean hasKey(Object nbt, String key) {
            try {
                return (boolean) HAS_KEY_METHOD.invoke(nbt, key);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }

        public static void setString(Object nbt, String key, String val) {
            try {
                SET_STRING_METHOD.invoke(nbt, key, val);
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }

        public static void setInt(Object nbt, String key, int val) {
            try {
                SET_INT_METHOD.invoke(nbt, key, val);
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }

        public static void setBoolean(Object nbt, String key, boolean val) {
            try {
                SET_BOOLEAN_METHOD.invoke(nbt, key, val);
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }

        public static void setDouble(Object nbt, String key, double val) {
            try {
                SET_DOUBLE_METHOD.invoke(nbt, key, val);
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }

        public static void setLong(Object nbt, String key, long val) {
            try {
                SET_LONG_METHOD.invoke(nbt, key, val);
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }

        public static double getDouble(Object nbt, String key) {
            try {
                return (double) GET_DOUBLE_METHOD.invoke(nbt, key);
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                e.printStackTrace();
            }
            throw new RuntimeException("Could not invoke getDouble");
        }

        public static int getInt(Object nbt, String key) {
            try {
                return (int) GET_INT_METHOD.invoke(nbt, key);
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                e.printStackTrace();
            }
            throw new RuntimeException("Could not invoke getInt");
        }

        public static long getLong(Object nbt, String key) {
            try {
                return (long) GET_LONG_METHOD.invoke(nbt, key);
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                e.printStackTrace();
            }
            throw new RuntimeException("Could not invoke getLong");
        }

        public static String getString(Object nbt, String key) {
            try {
                return (String) GET_STRING_METHOD.invoke(nbt, key);
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                e.printStackTrace();
            }
            throw new RuntimeException("Could not invoke getString");
        }

        public static Object getCompound(Object nbt, String key) {
            try {
                return GET_COMPOUND_METHOD.invoke(nbt, key);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
            throw new RuntimeException("Could not invoke getCompound");
        }

    }

    public static class Block {

        private static final Class<?> CLASS_CRAFT_CHEST = getOBC("block.CraftChest"),
                CLASS_TILE_ENTITY_CHEST = getNMS("TileEntityChest");
        private static final Method CRAFT_CHEST_GET_TILE_ENTITY = getMethod(CLASS_CRAFT_CHEST, "getTileEntity"),
                TILE_ENTITY_CHEST_SET_NAME = getMethod(CLASS_TILE_ENTITY_CHEST, "a", params(String.class)) != null ?
                        getMethod(CLASS_TILE_ENTITY_CHEST, "a", params(String.class)) :
                        getMethod(CLASS_TILE_ENTITY_CHEST, "setCustomName", params(String.class));

        public static void setChestName(Location loc, String newName) {
            setChestName(loc.getBlock(), newName);
        }

        public static void setChestName(org.bukkit.block.Block bl, String newName) {
//			if (TILE_ENTITY_CHEST_SET_NAME == null) {
//				Util.log("IT IS NULL AMK!");
//			}
            if (bl.getType() == Material.CHEST) {
                BlockState state = bl.getState();
                try {
                    Object tile = CRAFT_CHEST_GET_TILE_ENTITY.invoke(state);
//					System.out.println("THE TILE:" + tile);
//					System.out.println("THE METHOD:" + TILE_ENTITY_CHEST_SET_NAME);
                    TILE_ENTITY_CHEST_SET_NAME.invoke(tile, newName);
                } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }

    }

}
