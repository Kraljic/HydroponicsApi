package hr.kraljic.web.module.hydroponics.event;

import hr.kraljic.web.module.hydroponics.device.model.Device;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class DeviceEventPublisher {
    private final ApplicationEventPublisher applicationEventPublisher;

    public DeviceEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    public void publishDeviceAddedEvent(Device device) {
        DeviceAddedEvent deviceAddedEvent = new DeviceAddedEvent(this, device);
        applicationEventPublisher.publishEvent(deviceAddedEvent);
    }
}
