package com.example.dailyhelper.database.migration;

import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;
public class TodoMigration {
    // 버전 1에서 2로 마이그레이션
    public static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            // todo 테이블에 start_time과 end_time 컬럼을 추가
            database.execSQL("ALTER TABLE todo ADD COLUMN start_time TEXT");
            database.execSQL("ALTER TABLE todo ADD COLUMN end_time TEXT");
        }
    };
}
