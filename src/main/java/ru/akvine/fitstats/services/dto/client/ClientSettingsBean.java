package ru.akvine.fitstats.services.dto.client;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import ru.akvine.fitstats.entities.ClientSettingsEntity;
import ru.akvine.fitstats.enums.Language;
import ru.akvine.fitstats.enums.telegram.PrintMacronutrientsMode;

@Data
@Accessors(chain = true)
@NoArgsConstructor
public class ClientSettingsBean {
    private Long id;
    private String clientEmail;
    private Language language;
    private PrintMacronutrientsMode printMacronutrientsMode;
    private Integer roundAccuracy;
    private ClientBean clientBean;

    public ClientSettingsBean(ClientSettingsEntity clientSettingsEntity) {
        this.id = clientSettingsEntity.getId();
        this.language = clientSettingsEntity.getLanguage();
        this.printMacronutrientsMode = clientSettingsEntity.getPrintMacronutrientsMode();
        this.roundAccuracy = clientSettingsEntity.getRoundAccuracy();
        this.clientBean = new ClientBean(clientSettingsEntity.getClientEntity());
    }
}
