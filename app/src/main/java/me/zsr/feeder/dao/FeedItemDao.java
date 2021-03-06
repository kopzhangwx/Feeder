package me.zsr.feeder.dao;

import java.util.List;
import java.util.ArrayList;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.SqlUtils;
import de.greenrobot.dao.internal.DaoConfig;
import de.greenrobot.dao.query.Query;
import de.greenrobot.dao.query.QueryBuilder;

import me.zsr.feeder.dao.FeedItem;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table FEED_ITEM.
*/
public class FeedItemDao extends AbstractDao<FeedItem, String> {

    public static final String TABLENAME = "FEED_ITEM";

    /**
     * Properties of entity FeedItem.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Title = new Property(0, String.class, "title", true, "TITLE");
        public final static Property Link = new Property(1, String.class, "link", false, "LINK");
        public final static Property Description = new Property(2, String.class, "description", false, "DESCRIPTION");
        public final static Property Read = new Property(3, Boolean.class, "read", false, "READ");
        public final static Property Trash = new Property(4, Boolean.class, "trash", false, "TRASH");
        public final static Property Content = new Property(5, String.class, "content", false, "CONTENT");
        public final static Property LastShownDate = new Property(6, java.util.Date.class, "lastShownDate", false, "LAST_SHOWN_DATE");
        public final static Property Date = new Property(7, java.util.Date.class, "date", false, "DATE");
        public final static Property FeedSourceId = new Property(8, long.class, "feedSourceId", false, "FEED_SOURCE_ID");
    };

    private DaoSession daoSession;

    private Query<FeedItem> feedSource_FeedItemsQuery;

    public FeedItemDao(DaoConfig config) {
        super(config);
    }
    
    public FeedItemDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
        this.daoSession = daoSession;
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'FEED_ITEM' (" + //
                "'TITLE' TEXT PRIMARY KEY NOT NULL ," + // 0: title
                "'LINK' TEXT," + // 1: link
                "'DESCRIPTION' TEXT," + // 2: description
                "'READ' INTEGER," + // 3: read
                "'TRASH' INTEGER," + // 4: trash
                "'CONTENT' TEXT," + // 5: content
                "'LAST_SHOWN_DATE' INTEGER," + // 6: lastShownDate
                "'DATE' INTEGER," + // 7: date
                "'FEED_SOURCE_ID' INTEGER NOT NULL );"); // 8: feedSourceId
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'FEED_ITEM'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, FeedItem entity) {
        stmt.clearBindings();
        stmt.bindString(1, entity.getTitle());
 
        String link = entity.getLink();
        if (link != null) {
            stmt.bindString(2, link);
        }
 
        String description = entity.getDescription();
        if (description != null) {
            stmt.bindString(3, description);
        }
 
        Boolean read = entity.getRead();
        if (read != null) {
            stmt.bindLong(4, read ? 1l: 0l);
        }
 
        Boolean trash = entity.getTrash();
        if (trash != null) {
            stmt.bindLong(5, trash ? 1l: 0l);
        }
 
        String content = entity.getContent();
        if (content != null) {
            stmt.bindString(6, content);
        }
 
        java.util.Date lastShownDate = entity.getLastShownDate();
        if (lastShownDate != null) {
            stmt.bindLong(7, lastShownDate.getTime());
        }
 
        java.util.Date date = entity.getDate();
        if (date != null) {
            stmt.bindLong(8, date.getTime());
        }
        stmt.bindLong(9, entity.getFeedSourceId());
    }

    @Override
    protected void attachEntity(FeedItem entity) {
        super.attachEntity(entity);
        entity.__setDaoSession(daoSession);
    }

    /** @inheritdoc */
    @Override
    public String readKey(Cursor cursor, int offset) {
        return cursor.getString(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public FeedItem readEntity(Cursor cursor, int offset) {
        FeedItem entity = new FeedItem( //
            cursor.getString(offset + 0), // title
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // link
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // description
            cursor.isNull(offset + 3) ? null : cursor.getShort(offset + 3) != 0, // read
            cursor.isNull(offset + 4) ? null : cursor.getShort(offset + 4) != 0, // trash
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // content
            cursor.isNull(offset + 6) ? null : new java.util.Date(cursor.getLong(offset + 6)), // lastShownDate
            cursor.isNull(offset + 7) ? null : new java.util.Date(cursor.getLong(offset + 7)), // date
            cursor.getLong(offset + 8) // feedSourceId
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, FeedItem entity, int offset) {
        entity.setTitle(cursor.getString(offset + 0));
        entity.setLink(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setDescription(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setRead(cursor.isNull(offset + 3) ? null : cursor.getShort(offset + 3) != 0);
        entity.setTrash(cursor.isNull(offset + 4) ? null : cursor.getShort(offset + 4) != 0);
        entity.setContent(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setLastShownDate(cursor.isNull(offset + 6) ? null : new java.util.Date(cursor.getLong(offset + 6)));
        entity.setDate(cursor.isNull(offset + 7) ? null : new java.util.Date(cursor.getLong(offset + 7)));
        entity.setFeedSourceId(cursor.getLong(offset + 8));
     }
    
    /** @inheritdoc */
    @Override
    protected String updateKeyAfterInsert(FeedItem entity, long rowId) {
        return entity.getTitle();
    }
    
    /** @inheritdoc */
    @Override
    public String getKey(FeedItem entity) {
        if(entity != null) {
            return entity.getTitle();
        } else {
            return null;
        }
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
    /** Internal query to resolve the "feedItems" to-many relationship of FeedSource. */
    public List<FeedItem> _queryFeedSource_FeedItems(long feedSourceId) {
        synchronized (this) {
            if (feedSource_FeedItemsQuery == null) {
                QueryBuilder<FeedItem> queryBuilder = queryBuilder();
                queryBuilder.where(Properties.FeedSourceId.eq(null));
                queryBuilder.orderRaw("DATE DESC");
                feedSource_FeedItemsQuery = queryBuilder.build();
            }
        }
        Query<FeedItem> query = feedSource_FeedItemsQuery.forCurrentThread();
        query.setParameter(0, feedSourceId);
        return query.list();
    }

    private String selectDeep;

    protected String getSelectDeep() {
        if (selectDeep == null) {
            StringBuilder builder = new StringBuilder("SELECT ");
            SqlUtils.appendColumns(builder, "T", getAllColumns());
            builder.append(',');
            SqlUtils.appendColumns(builder, "T0", daoSession.getFeedSourceDao().getAllColumns());
            builder.append(" FROM FEED_ITEM T");
            builder.append(" LEFT JOIN FEED_SOURCE T0 ON T.'FEED_SOURCE_ID'=T0.'_id'");
            builder.append(' ');
            selectDeep = builder.toString();
        }
        return selectDeep;
    }
    
    protected FeedItem loadCurrentDeep(Cursor cursor, boolean lock) {
        FeedItem entity = loadCurrent(cursor, 0, lock);
        int offset = getAllColumns().length;

        FeedSource feedSource = loadCurrentOther(daoSession.getFeedSourceDao(), cursor, offset);
         if(feedSource != null) {
            entity.setFeedSource(feedSource);
        }

        return entity;    
    }

    public FeedItem loadDeep(Long key) {
        assertSinglePk();
        if (key == null) {
            return null;
        }

        StringBuilder builder = new StringBuilder(getSelectDeep());
        builder.append("WHERE ");
        SqlUtils.appendColumnsEqValue(builder, "T", getPkColumns());
        String sql = builder.toString();
        
        String[] keyArray = new String[] { key.toString() };
        Cursor cursor = db.rawQuery(sql, keyArray);
        
        try {
            boolean available = cursor.moveToFirst();
            if (!available) {
                return null;
            } else if (!cursor.isLast()) {
                throw new IllegalStateException("Expected unique result, but count was " + cursor.getCount());
            }
            return loadCurrentDeep(cursor, true);
        } finally {
            cursor.close();
        }
    }
    
    /** Reads all available rows from the given cursor and returns a list of new ImageTO objects. */
    public List<FeedItem> loadAllDeepFromCursor(Cursor cursor) {
        int count = cursor.getCount();
        List<FeedItem> list = new ArrayList<FeedItem>(count);
        
        if (cursor.moveToFirst()) {
            if (identityScope != null) {
                identityScope.lock();
                identityScope.reserveRoom(count);
            }
            try {
                do {
                    list.add(loadCurrentDeep(cursor, false));
                } while (cursor.moveToNext());
            } finally {
                if (identityScope != null) {
                    identityScope.unlock();
                }
            }
        }
        return list;
    }
    
    protected List<FeedItem> loadDeepAllAndCloseCursor(Cursor cursor) {
        try {
            return loadAllDeepFromCursor(cursor);
        } finally {
            cursor.close();
        }
    }
    

    /** A raw-style query where you can pass any WHERE clause and arguments. */
    public List<FeedItem> queryDeep(String where, String... selectionArg) {
        Cursor cursor = db.rawQuery(getSelectDeep() + where, selectionArg);
        return loadDeepAllAndCloseCursor(cursor);
    }
 
}
