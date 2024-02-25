package ru.akvine.fitstats.services.dto.barcode;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import ru.akvine.fitstats.entities.BarCodeEntity;
import ru.akvine.fitstats.enums.BarCodeType;
import ru.akvine.fitstats.services.dto.base.SoftBean;
import ru.akvine.fitstats.services.dto.product.ProductBean;

@Data
@Accessors(chain = true)
@NoArgsConstructor
public class BarCodeBean extends SoftBean {
    private Long id;
    private String number;
    private BarCodeType barCodeType;
    private ProductBean productBean;

    public BarCodeBean(BarCodeEntity barCodeEntity) {
        this.id = barCodeEntity.getId();
        this.number = barCodeEntity.getNumber();
        this.barCodeType = barCodeEntity.getType();
        this.productBean = new ProductBean(barCodeEntity.getProduct());

        this.updatedDate = barCodeEntity.getUpdatedDate();
        this.deleted = barCodeEntity.isDeleted();
        this.deletedDate = barCodeEntity.getDeletedDate();
    }
}
