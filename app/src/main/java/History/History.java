package history;

public class History {

    String userKey;
    String name;
    String date;
    String manu;
    int count;
    int pprice;
    int tprice;

    public History(String userKey, String date, int count, int pprice, int tprice, String name, String manu) {

        this.userKey = userKey;
        this.name = name;
        this.date = date;
        this.manu = manu;
        this.count = count;
        this.pprice = pprice;
        this.tprice = tprice;
    }

    public String getuserkey() {
        return userKey;
    }

    public void setUserKey(String userKey) {
        this.userKey = userKey;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getManu() {
        return manu;
    }

    public void setManu(String manu) {
        this.manu = manu;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getPprice() {
        return pprice;
    }

    public void setPprice(int pprice) {
        this.pprice = pprice;
    }

    public int getTprice() {
        return tprice;
    }

    public void setTprice(int tprice) {
        this.tprice = tprice;
    }

}
