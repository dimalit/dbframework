package edu.dbframework.parse.beans.items;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class ColumnItem {

	private String name;
	private String alias;
	private String indexTableName;
	private String indexColumnName;
    private Boolean primaryKey;

	public ColumnItem() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getIndexTableName() {
		return indexTableName;
	}

	public void setIndexTableName(String indexTableName) {
		this.indexTableName = indexTableName;
	}

	public String getIndexColumnName() {
		return indexColumnName;
	}

	public void setIndexColumnName(String indexColumnName) {
		this.indexColumnName = indexColumnName;
	}

    public Boolean getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(Boolean primaryKey) {
        this.primaryKey = primaryKey;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(this.name)
                .append(this.alias)
                .append(this.indexTableName)
                .append(this.indexColumnName)
                .append(this.primaryKey)
                .toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        if (!(obj instanceof ColumnItem))
            return false;
        ColumnItem otherObj = (ColumnItem) obj;
        return new EqualsBuilder()
                .append(this.name, otherObj.name)
                .append(this.alias, otherObj.alias)
                .append(this.indexTableName, otherObj.indexTableName)
                .append(this.indexColumnName, otherObj.indexColumnName)
                .append(this.primaryKey, otherObj.primaryKey)
                .isEquals();
    }
}
