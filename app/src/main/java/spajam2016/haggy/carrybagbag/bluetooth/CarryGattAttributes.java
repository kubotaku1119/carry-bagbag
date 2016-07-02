package spajam2016.haggy.carrybagbag.bluetooth;

import java.util.UUID;

/**
 * BLE GATT define class.
 */
public class CarryGattAttributes {

    public static final String MANUFACTURER_NAME = "00002a29-0000-1000-8000-00805f9b34fb";

    public static final String CLIENT_CHARACTERISTIC_CONFIG = "00002902-0000-1000-8000-00805f9b34fb";

    public static final String CARRY_SERVICE = "19B10000-E8F2-537E-4F6C-D104768A1214";

    public static final String CARRY_CHARA = "19B10001-E8F2-537E-4F6C-D104768A1214";

    public static final UUID UUID_KASA_SERVICE = UUID.fromString(CARRY_SERVICE);

    public static final UUID UUID_KASA_CHARA = UUID.fromString(CARRY_CHARA);



}
