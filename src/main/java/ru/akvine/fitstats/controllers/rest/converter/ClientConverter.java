package ru.akvine.fitstats.controllers.rest.converter;

import com.google.common.base.Preconditions;
import org.springframework.stereotype.Component;
import ru.akvine.fitstats.controllers.rest.dto.client.ClientDto;
import ru.akvine.fitstats.controllers.rest.dto.client.ClientLoginRequest;
import ru.akvine.fitstats.controllers.rest.dto.client.ClientRegisterRequest;
import ru.akvine.fitstats.controllers.rest.dto.client.ClientResponse;
import ru.akvine.fitstats.enums.*;
import ru.akvine.fitstats.services.dto.client.ClientBean;
import ru.akvine.fitstats.services.dto.client.ClientRegister;

@Component
public class ClientConverter {
    public ClientRegister convertToClientRegister(ClientRegisterRequest request) {
        Preconditions.checkNotNull(request, "clientRegisterRequest is null");
        return new ClientRegister()
                .setEmail(request.getEmail())
                .setPassword(request.getPassword())
                .setFirstName(request.getFirstName())
                .setSecondName(request.getSecondName())
                .setThirdName(request.getThirdName())
                .setAge(request.getAge())
                .setWeight(request.getWeight())
                .setHeight(request.getHeight())
                .setGender(Gender.valueOf(request.getGender()))
                .setPhysicalActivity(PhysicalActivity.valueOf(request.getPhysicalActivity()))
                .setHeightMeasurement(HeightMeasurement.valueOf(request.getHeightMeasurement()))
                .setWeightMeasurement(WeightMeasurement.valueOf(request.getWeightMeasurement()))
                .setDiet(Diet.valueOf(request.getDiet()));
    }

    public ClientBean convertToClientBean(ClientLoginRequest request) {
        Preconditions.checkNotNull(request, "clientLoginRequest is null");
        return new ClientBean()
                .setEmail(request.getEmail())
                .setPassword(request.getPassword());
    }

    public ClientResponse convertToClientResponse(ClientBean clientBean) {
        Preconditions.checkNotNull(clientBean, "clientBean is null");
        return new ClientResponse()
                .setClient(buildClientDto(clientBean));
    }

    private ClientDto buildClientDto(ClientBean clientBean) {
        return new ClientDto()
                .setUuid(clientBean.getUuid())
                .setEmail(clientBean.getEmail())
                .setFirstName(clientBean.getFirstName())
                .setSecondName(clientBean.getSecondName())
                .setThirdName(clientBean.getThirdName());
    }
}
