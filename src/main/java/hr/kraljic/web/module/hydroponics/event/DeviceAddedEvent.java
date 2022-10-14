package hr.kraljic.web.module.hydroponics.event;

import hr.kraljic.web.module.hydroponics.device.model.Device;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class DeviceAddedEvent extends ApplicationEvent {
    private Device device;

    public DeviceAddedEvent(Object source, Device device) {
        super(source);
        this.device = device;
    }
}
