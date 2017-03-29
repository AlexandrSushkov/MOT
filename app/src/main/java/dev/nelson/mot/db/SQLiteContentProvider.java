package dev.nelson.mot.db;

import android.content.ComponentName;
import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.database.SQLException;
import android.net.Uri;
import android.provider.BaseColumns;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import dev.nelson.mot.db.model.CategoriesProvider;
import dev.nelson.mot.db.model.MultipleTablesProvider;
import dev.nelson.mot.db.model.SQLiteTableProvider;
import dev.nelson.mot.db.model.PaymentsProvider;
import dev.nelson.mot.exeption.NoSuchTableException;
import dev.nelson.mot.exeption.UnknownUriException;

public class SQLiteContentProvider extends ContentProvider {

    public static final Map<String, SQLiteTableProvider> SCHEMA = new ConcurrentHashMap<>();

    static {
        SCHEMA.put(PaymentsProvider.TABLE_NAME, new PaymentsProvider());
        SCHEMA.put(CategoriesProvider.TABLE_NAME, new CategoriesProvider());
        SCHEMA.put(MultipleTablesProvider.MULTIPLE_TABLES, new MultipleTablesProvider());
    }

    private static final String MIME_ITEM = "vnd.android.cursor.item/";
    private static final String MIME_DIR = "vnd.android.cursor.dir/";

    private static ProviderInfo getProviderInfo(Context context, Class<? extends ContentProvider> contentProvider, int flag) throws PackageManager.NameNotFoundException {
        return context.getPackageManager().getProviderInfo(new ComponentName(context.getPackageName(), contentProvider.getName()), flag);
    }

    private static String getTableName(Uri uri) {
        return uri.getPathSegments().get(0);
    }

    private final SQLUriMatcher mUriMatcher = new SQLUriMatcher();
    private SQLiteOpenHelperImpl mHelper;

    @Override
    public boolean onCreate() {
        try {
            ProviderInfo providerInfo = getProviderInfo(getContext(), getClass(), 0);
            String[] authorities = TextUtils.split(providerInfo.authority, ";");
            for (String authority : authorities) {
                mUriMatcher.addAuthority(authority);
            }
            mHelper = new SQLiteOpenHelperImpl(getContext());
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            throw new SQLException(e.getMessage());
        }
    }

    @Nullable
    @Override
    @SuppressWarnings("ConstantConditions")
    public Cursor query(@NonNull Uri uri, String[] columns, String where, String[] whereArgs, String sortOrder) {
        int matchResult = mUriMatcher.match(uri);
        if (matchResult == SQLUriMatcher.NO_MATCH) {
            throw new UnknownUriException(uri.toString());
        }
        String tableName = getTableName(uri);
        final SQLiteTableProvider tableProvider = SCHEMA.get(tableName);
        if (tableProvider == null) {
            throw new SQLException("No such table " + tableName);
        }
        if (matchResult == SQLUriMatcher.MATCH_ID) {
            where = BaseColumns._ID + "=?";
            whereArgs = new String[]{uri.getLastPathSegment()};
        }
        Cursor cursor = tableProvider.query(mHelper.getReadableDatabase(), columns, where, whereArgs, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    @SuppressWarnings("ConstantConditions")
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        final int matchResults = mUriMatcher.match(uri);
        if (matchResults == SQLUriMatcher.NO_MATCH) {
            throw new UnknownUriException(uri.toString());
        }
        final String tableName = getTableName(uri);
        final SQLiteTableProvider tableProvider = SCHEMA.get(tableName);
        if (tableProvider == null) {
            throw new NoSuchTableException(tableName);
        }
        final long lastId = tableProvider.insert(mHelper.getReadableDatabase(), values);
        Uri resultUri = ContentUris.withAppendedId(uri, lastId);
        getContext().getContentResolver().notifyChange(resultUri, null);
        return resultUri;
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    public int update(@NonNull Uri uri, ContentValues values, String where, String[] whereArgs) {
        final int matchResult = mUriMatcher.match(uri);
        if (matchResult == SQLUriMatcher.NO_MATCH) {
            throw new UnknownUriException(uri.toString());
        }
        final String tableName = getTableName(uri);
        final SQLiteTableProvider tableProvider = SCHEMA.get(tableName);
        if (tableProvider == null) {
            throw new NoSuchTableException(tableName);
        }
        if (matchResult == SQLUriMatcher.MATCH_ID) {
            where = BaseColumns._ID + "=?";
            whereArgs = new String[]{uri.getLastPathSegment()};
        }
        final int affectedRow = tableProvider.update(mHelper.getReadableDatabase(), values, where, whereArgs);
        getContext().getContentResolver().notifyChange(tableProvider.getBaseUri(), null);
        return affectedRow;
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    public int delete(@NonNull Uri uri, String where, String[] whereArgs) {
        final int matchResult = mUriMatcher.match(uri);
        if (matchResult == SQLUriMatcher.NO_MATCH) {
            throw new UnknownUriException(uri.toString());
        }
        final String tableName = getTableName(uri);
        final SQLiteTableProvider tableProvider = SCHEMA.get(tableName);
        if (tableProvider == null) {
            throw new NoSuchTableException(uri.toString());
        }
        if (matchResult == SQLUriMatcher.MATCH_ID) {
            where = BaseColumns._ID + "=?";
            whereArgs = new String[]{uri.getLastPathSegment()};
        }
        final int affectedRow = tableProvider.delete(mHelper.getReadableDatabase(), where, whereArgs);
        getContext().getContentResolver().notifyChange(tableProvider.getBaseUri(), null);
        return affectedRow;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        final int matchResult = mUriMatcher.match(uri);
        if (matchResult == SQLUriMatcher.NO_MATCH) {
            throw new UnknownUriException(uri.toString());
        } else if (matchResult == SQLUriMatcher.MATCH_ID) {
            return MIME_ITEM + getTableName(uri);
        }
        return MIME_DIR + getTableName(uri);
    }
}
