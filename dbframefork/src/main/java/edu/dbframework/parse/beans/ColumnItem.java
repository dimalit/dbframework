package edu.dbframework.parse.beans;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class ColumnItem {

	private String name;
	private String alias;
	private String relationTableName;
	private String relationColumnName;
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

	public String getRelationTableName() {
		return relationTableName;
	}

	public void setRelationTableName(String relationTableName) {
		this.relationTableName = relationTableName;
	}

	public String getRelationColumnName() {
		return relationColumnName;
	}

	public void setRelationColumnName(String relationColumnName) {
		this.relationColumnName = relationColumnName;
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
                .append(this.relationTableName)
                .append(this.relationColumnName)
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
                .append(this.relationTableName, otherObj.relationTableName)
                .append(this.relationColumnName, otherObj.relationColumnName)
                .append(this.primaryKey, otherObj.primaryKey)
                .isEquals();
    }
}
