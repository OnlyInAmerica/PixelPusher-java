package com.heroicrobot.dropbit.devices.pixelpusher;

import java.util.Arrays;

public class PusherCommand {


 /*  const uint8_t pp_command_magic[16] = 
  *   { 0x40, 0x09, 0x2d, 0xa6, 0x15, 0xa5, 0xdd, 0xe5, 0x6a, 0x9d, 0x4d, 0x5a, 0xcf, 0x09, 0xaf, 0x50  };
  *
  * #define COMMAND_RESET                0x01
  * #define COMMAND_GLOBALBRIGHTNESS_SET 0x02
  */
  
  private static final byte pp_command_magic[] = 
    { (byte) 0x40, (byte) 0x09, (byte) 0x2d, (byte) 0xa6, (byte) 0x15, (byte) 0xa5, (byte) 0xdd, (byte) 0xe5,
      (byte) 0x6a, (byte) 0x9d, (byte)0x4d, (byte)0x5a, (byte) 0xcf, (byte) 0x09,(byte) 0xaf,(byte) 0x50  };

  public static final byte RESET = 0x01;
  public static final byte GLOBALBRIGHTNESS_SET = 0x02;
  public static final byte WIFI_CONFIGURE = 0x03;
  
  public byte command;
  private short parameter;
  private String ssid;
  private String key;
  private byte  security;
  
/*  enum Security {
    NONE = 0,
    WEP  = 1,
    WPA  = 2,
    WPA2 = 3
 };
  */
  
  PusherCommand(byte command) {
    this.command = command;
  }
  
  PusherCommand(byte command, short parameter) {
    this.command = command;
    this.parameter = parameter;
  }
  
  PusherCommand(byte command, String ssid, String key, String security) {
    this.command = command;
    this.ssid = ssid;
    this.key = key;
    if (security.toLowerCase().compareTo("none") == 0) 
      this.security = 0;
    if (security.toLowerCase().compareTo("wep") == 0) 
      this.security = 1;
    if (security.toLowerCase().compareTo("wpa") == 0) 
      this.security = 2;
    if (security.toLowerCase().compareTo("wpa2") == 0) 
      this.security = 3;
  }
  
  public byte [] generateBytes() {
    byte[] returnVal= null;
    if (command == RESET) {
      returnVal = Arrays.copyOf(pp_command_magic, pp_command_magic.length+1);
      returnVal[pp_command_magic.length] = RESET;
    } else if (command == GLOBALBRIGHTNESS_SET) {
      returnVal = Arrays.copyOf(pp_command_magic, pp_command_magic.length+3);
      returnVal[pp_command_magic.length] = GLOBALBRIGHTNESS_SET;
      returnVal[pp_command_magic.length+1] = (byte) (parameter & 0xff);
      returnVal[pp_command_magic.length+1] = (byte) ((parameter>>8) & 0xff);
    } else if (command == WIFI_CONFIGURE) {
      byte[] ssidBytes = ssid.getBytes();
      byte[] keyBytes = key.getBytes();
      int bufLength = 0;
      bufLength += (pp_command_magic.length) + 1; /* length of command */
      bufLength += 1; // length of key type
      bufLength += ssidBytes.length + 1; // ssid plus null terminator
      bufLength += keyBytes.length + 1;  // key plus null terminator
      returnVal = Arrays.copyOf(pp_command_magic, bufLength);
      
      returnVal[pp_command_magic.length] = command;
      
      for (int i=0; i<ssidBytes.length; i++ )
        returnVal[pp_command_magic.length+ 1 + i] = ssidBytes[i];
      
      for (int i=0; i<keyBytes.length; i++ )
        returnVal[pp_command_magic.length+ 1 + keyBytes.length + 1 + i] = ssidBytes[i];
      
      returnVal[pp_command_magic.length+ 1 + keyBytes.length + 1 + ssidBytes.length + 1] = security;
    }
    return returnVal;
  }
}
