package com.heroicrobot.dropbit.discovery;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;

import org.joda.time.DateTime;
import org.joda.time.Minutes;
import org.joda.time.Seconds;

import com.heroicrobot.dropbit.devices.Device;
import com.heroicrobot.dropbit.devices.PixelPusher;

import hypermedia.net.UDP;

public class DeviceRegistry extends Observable {

  private UDP udp;
  private static final int DISCOVERY_PORT = 7331;
  private static final int MAX_DISCONNECT_SECONDS = 30;

  private Map<String, Device> deviceMap;
  private Map<String, DateTime> deviceLastSeenMap;

  public Map<String, Device> getDeviceMap() {
    return deviceMap;
  }

  class DeviceExpiryTask implements Runnable {

    private DeviceRegistry registry;
    
    DeviceExpiryTask(DeviceRegistry registry) {
      this.registry = registry;
    }
    
    @Override
    public void run() {
      for (String deviceMac : deviceMap.keySet()) {
        // foo
        Seconds lastSeenSeconds = Seconds.secondsBetween(
            deviceLastSeenMap.get(deviceMac), DateTime.now());
        if (lastSeenSeconds.getSeconds() > MAX_DISCONNECT_SECONDS) {
          deviceMap.remove(deviceMac);
          deviceLastSeenMap.remove(deviceMac);
          registry.setChanged();
        }
      }
    }

  }

  public DeviceRegistry() {
    udp = new UDP(this, DISCOVERY_PORT);
    deviceMap = new HashMap<String, Device>();
    udp.setReceiveHandler("receive");
    udp.log(true);
    udp.listen(true);
  }

  public void receive(byte[] data) {
    System.out.println("Got data");
    DeviceHeader header = new DeviceHeader(data);
    String macAddr = header.GetMacAddressString();
    Device device = null;
    if (header.DeviceType == DeviceType.PIXELPUSHER) {
      device = new PixelPusher(header.PacketRemainder);
    }
    // Set the timestamp for the last time this device checked in
    deviceLastSeenMap.put(macAddr, DateTime.now());
    if (!deviceMap.containsKey(macAddr)) {
      // We haven't seen this device before
      deviceMap.put(macAddr, device);
      this.setChanged();
    } else {
      if (deviceMap.get(macAddr) != device) {
        // We already knew about this device at the given MAC, but its details
        // have changed
        deviceMap.put(macAddr, device);
        this.setChanged();
      } else {
        // The device is identical, nothing has changed
      }
    }
  }

}
