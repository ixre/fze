package net.fze.ext.hibernate;

 class TestUser  {
    private int id;

    @javax.persistence.Id
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
