package kopacz.diabetool;

import java.io.Serializable;
import java.util.Date;

abstract class DatabaseEntry implements Serializable {
    private long id;
    private long rid;
    //protected String name;
    private boolean sent;

    private long date_created;
    private boolean favourite;

    DatabaseEntry ()
    {
        this.id = -1;
        this.rid = 0; // 0 działa jak null
        this.sent = false;
        this.date_created = System.currentTimeMillis();
        this.favourite = false;
    }

    DatabaseEntry(long id, long rid, boolean sent, long date_created, boolean favourite) {
        this.id = id;
        this.rid = rid;
        this.sent = sent;
        this.date_created = date_created;
        this.favourite = favourite;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getRid() {
        return rid;
    }

    public void setRid(long rid) {
        this.rid = rid;
    }

    public boolean isSent() {
        return sent;
    }

    public void setSent(boolean sent) {
        this.sent = sent;
    }

    public long getDateCreatedRaw() {
        return date_created;
    }

    public Date getDateCreated ()
    {
        return new Date(date_created);
    }

    public void setDateCreated(long date_created) {
        this.date_created = date_created;
    }

    public boolean isFavourite() {
        return favourite;
    }

    public void setFavourite(boolean favourite) {
        this.favourite = favourite;
    }
}
