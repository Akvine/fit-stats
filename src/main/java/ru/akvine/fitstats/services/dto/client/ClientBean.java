package ru.akvine.fitstats.services.dto.client;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.Nullable;
import ru.akvine.fitstats.entities.ClientEntity;
import ru.akvine.fitstats.enums.Diet;
import ru.akvine.fitstats.services.dto.base.SoftBean;

@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
public class ClientBean extends SoftBean {
    private Long id;
    private String uuid;
    private String firstName;
    private String secondName;
    @Nullable
    private String thirdName;
    private String email;
    @Nullable
    private String password;
    @Nullable
    private String hash;
    private Diet diet;

    public ClientBean(ClientEntity clientEntity) {
        this.id = clientEntity.getId();
        this.uuid = clientEntity.getUuid();
        this.firstName = clientEntity.getFirstName();
        this.secondName = clientEntity.getSecondName();
        this.thirdName = clientEntity.getThirdName();
        this.email = clientEntity.getEmail();
        this.hash = clientEntity.getHash();

        this.createdDate = clientEntity.getCreatedDate();
        this.updatedDate = clientEntity.getUpdatedDate();
        this.deletedDate = clientEntity.getDeletedDate();
        this.deleted = clientEntity.isDeleted();
    }
}
