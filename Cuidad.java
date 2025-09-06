class Ciudad {
    int id;
    double x;
    double y;

    public Ciudad(int id, double x, double y) {
        this.id = id;
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return "Ciudad{" + "id=" + id + ", x=" + x + ", y=" + y + '}';
    }
}