package me.ufo.tools.util.items;

public class NBTObject<This extends NBTObject<This>> {

    Object NBT;

    NBTObject() {
    }

    public NBTObject(Object NMS_NBT) {
        this.NBT = NMS_NBT;
    }

    public String getString(String key) {
        return Ref.NBT.getString(NBT, key);
    }

    public This setString(String key, String value) {
        Ref.NBT.setString(NBT, key, value);
        return (This) this;
    }

    public int getInt(String key) {
        return Ref.NBT.getInt(NBT, key);
    }

    public This setInt(String key, int value) {
        Ref.NBT.setInt(NBT, key, value);
        return (This) this;
    }

    public double getDouble(String key) {
        return Ref.NBT.getDouble(NBT, key);
    }

    public This setDouble(String key, double value) {
        Ref.NBT.setDouble(NBT, key, value);
        return (This) this;
    }

    public long getLong(String key) {
        return Ref.NBT.getLong(NBT, key);
    }

    public This setLong(String key, long value) {
        Ref.NBT.setLong(NBT, key, value);
        return (This) this;
    }

    public boolean hasKey(String key) {
        return Ref.NBT.hasKey(NBT, key);
    }

    public NBTObject getNBT(String key) {
        return new NBTObject(Ref.NBT.getCompound(NBT, key));
    }

//    public List<String> getStringList(String key) {
//        return Ref.NBT.get
//    }

}
