package hr.kraljic.web.module.hydroponics.sensortype;

import hr.kraljic.web.module.hydroponics.exception.SensorTypeDeleteIntegrityViolationApiException;
import hr.kraljic.web.module.hydroponics.exception.SensorTypeNotFoundApiException;
import hr.kraljic.web.module.hydroponics.sensortype.model.SensorType;
import hr.kraljic.web.module.hydroponics.sensortype.model.SensorTypeCommand;
import hr.kraljic.web.module.hydroponics.sensortype.model.SensorTypeDto;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SensorTypeServiceImpl implements SensorTypeService {
    private final SensorTypeRepository sensorTypeRepository;

    public SensorTypeServiceImpl(SensorTypeRepository sensorTypeRepository) {
        this.sensorTypeRepository = sensorTypeRepository;
    }

    @Override
    public List<SensorTypeDto> getAllSensorTypes() {
        return sensorTypeRepository.findAll()
                .stream()
                .map(SensorTypeDto::map)
                .collect(Collectors.toList());
    }

    @Override
    public SensorTypeDto getSensorTypeById(Integer sensorTypeId) {
        SensorType sensorType = findSensorTypeById(sensorTypeId);

        return SensorTypeDto.map(sensorType);
    }

    @Override
    public SensorTypeDto createSensorType(SensorTypeCommand sensorTypeCommand) {
        SensorType sensorType = mapToSensorType(new SensorType(), sensorTypeCommand);

        sensorType = sensorTypeRepository.save(sensorType);

        return SensorTypeDto.map(sensorType);
    }

    @Override
    public SensorTypeDto updateSensorType(Integer sensorTypeId, SensorTypeCommand sensorTypeCommand) {
        SensorType sensorType = findSensorTypeById(sensorTypeId);
        sensorType = mapToSensorType(sensorType, sensorTypeCommand);

        sensorType = sensorTypeRepository.save(sensorType);

        return SensorTypeDto.map(sensorType);
    }

    @Override
    public void deleteSensorType(Integer sensorTypeId) {
        SensorType sensorType = findSensorTypeById(sensorTypeId);

        try {
            sensorTypeRepository.deleteById(sensorType.getId());
        } catch (DataIntegrityViolationException e) {
            throw new SensorTypeDeleteIntegrityViolationApiException(sensorType.getType());
        }
    }

    private SensorType findSensorTypeById(Integer id) {
        return sensorTypeRepository.findById(id)
                .orElseThrow(() -> new SensorTypeNotFoundApiException(id));
    }

    private SensorType mapToSensorType(SensorType sensorType, SensorTypeCommand sensorTypeCommand) {
        sensorType.setType(sensorTypeCommand.getType());
        sensorType.setMeasuringUnit(sensorTypeCommand.getMeasuringUnit());

        return sensorType;
    }
}
