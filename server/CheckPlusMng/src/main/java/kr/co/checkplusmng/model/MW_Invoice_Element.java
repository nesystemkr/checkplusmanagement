package kr.co.checkplusmng.model;

import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.FullEntity;
import com.google.cloud.datastore.KeyFactory;

public class MW_Invoice_Element extends MW_BaseModel {
	private long invoiceIdKey;
	private long elementIdKey;
	public long getInvoiceIdKey() {
		return invoiceIdKey;
	}
	public void setInvoiceIdKey(long invoiceIdKey) {
		this.invoiceIdKey = invoiceIdKey;
	}
	public long getElementIdKey() {
		return elementIdKey;
	}
	public void setElementIdKey(long elementIdKey) {
		this.elementIdKey = elementIdKey;
	}
	@Override
	public FullEntity<?> toEntity(KeyFactory keyFactory) {
		return super.builder(keyFactory)
				.set("invoiceIdKey", invoiceIdKey)
				.set("elementIdKey", elementIdKey)
				.build();
	}
	@Override
	public Entity toEntity(Entity existOne) {
		return super.builder(existOne)
				.set("invoiceIdKey", invoiceIdKey)
				.set("elementIdKey", elementIdKey)
				.build();
	}
	@Override
	public MW_Invoice_Element fromEntity(Entity entity) {
		super.fromEntity(entity);
		setInvoiceIdKey(entity.getLong("invoiceIdKey"));
		setElementIdKey(entity.getLong("elementIdKey"));
		return this;
	}

}
