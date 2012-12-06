package com.hwy.checklist;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {

	public DbHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

		String sql = "CREATE TABLE CheckList_List (List_id INTEGER PRIMARY KEY AUTOINCREMENT,Int_id INTEGER DEFAULT 0, List_Name TEXT, List_Type INTEGER, Pw INTEGER DEFAULT 0, Createtime INTEGER DEFAULT 0)";
		String sql2 = "CREATE TABLE CheckList_BbqListitems (List_id2 INTEGER, Food_id INTEGER, Checkstate INTEGER, Price INTEGER, Qty INTEGER, Send INTEGER DEFAULT 0)";
		String sql3 = "CREATE TABLE CheckList_HotListitems (List_id2 INTEGER, Food_id INTEGER, Checkstate INTEGER, Price INTEGER, Qty INTEGER, Send INTEGER DEFAULT 0)";
		String sql4 = "CREATE TABLE CheckList_Bbq_Food (Food_id INTEGER PRIMARY KEY, Types_id INTEGER, Name TEXT, Vote INTEGER, Report INTEGER)";
		String sql5 = "INSERT INTO CheckList_Bbq_Food SELECT 1 as 'Food_id', 1 as Types_id, '�j�յ�' as Name, 0 as Vote, 0 as Report UNION SELECT 2, 1, '��ۣ', 0, 0 UNION SELECT 3, 1, '�A�Vۣ', 0, 0 UNION SELECT 4, 1, '�v��', 0, 0 UNION SELECT 5, 1, '�X�l', 0, 0 UNION SELECT 6, 1, '����', 0, 0 UNION SELECT 7, 1, '��ۣ', 0, 0 UNION SELECT 8, 1, '�G��', 0, 0 UNION SELECT 9, 1, '����', 0, 0 UNION SELECT 10, 1, '���X', 0, 0 UNION SELECT 11, 2, '�N��', 0, 0 UNION SELECT 12, 2, '���Y', 0, 0 UNION SELECT 13, 2, '�����Y', 0, 0 UNION SELECT 14, 2, '��ۣ�^�Y', 0, 0 UNION SELECT 15, 2, '�^�Y', 0, 0 UNION SELECT 16, 2, '���J', 0, 0 UNION SELECT 17, 2, '���Y', 0, 0 UNION SELECT 18, 2, '�����Y', 0, 0 UNION SELECT 19, 3, '����', 0, 0 UNION SELECT 20, 3, '����', 0, 0 UNION SELECT 21, 3, '���x', 0, 0 UNION SELECT 22, 3, '�Ϭh', 0, 0 UNION SELECT 23, 3, '�ϵ�', 0, 0 UNION SELECT 24, 3, '�ۤh�z', 0, 0 UNION SELECT 25, 3, '���F��', 0, 0 UNION SELECT 26, 3, '���z', 0, 0 UNION SELECT 27, 3, '��l����', 0, 0 UNION SELECT 28, 3, '�~����', 0, 0 UNION SELECT 29, 3, '�e�Ĥ��h', 0, 0 UNION SELECT 30, 3, '�z�J', 0, 0 UNION SELECT 31, 3, '�ޥ�', 0, 0 UNION SELECT 32, 3, '�ެh', 0, 0 UNION SELECT 33, 3, '�����l', 0, 0 UNION SELECT 34, 3, '����', 0, 0 UNION SELECT 35, 3, '�����l', 0, 0 UNION SELECT 36, 3, '���׸z', 0, 0 UNION SELECT 37, 3, '���ݦ�', 0, 0 UNION SELECT 38, 3, '���l', 0, 0 UNION SELECT 39, 3, '�нޤ�', 0, 0 UNION SELECT 40, 4, '�j����', 0, 0 UNION SELECT 57, 4, '�h�K��', 0, 0 UNION SELECT 58, 4, '�F�l��', 0, 0 UNION SELECT 59, 4, '�C�f', 0, 0 UNION SELECT 60, 4, '��M��', 0, 0 UNION SELECT 61, 4, '�Q�Y', 0, 0 UNION SELECT 62, 4, '�a�l', 0, 0 UNION SELECT 63, 4, '���h', 0, 0 UNION SELECT 64, 4, '�t�l', 0, 0 UNION SELECT 65, 4, '��', 0, 0 UNION SELECT 66, 4, '�{��', 0, 0 UNION SELECT 67, 4, '�{����', 0, 0 UNION SELECT 68, 4, '�s��', 0, 0 UNION SELECT 69, 4, '��', 0, 0 UNION SELECT 70, 4, 'Į', 0, 0 UNION SELECT 71, 5, '���o', 0, 0 UNION SELECT 72, 5, '�F�R��', 0, 0 UNION SELECT 73, 5, '�T��', 0, 0 UNION SELECT 74, 5, '���G�R', 0, 0 UNION SELECT 76, 5, '����', 0, 0 UNION SELECT 77, 5, '��s', 0, 0 UNION SELECT 78, 5, '����', 0, 0 UNION SELECT 79, 5, '�[�T', 0, 0 UNION SELECT 80, 5, '�]�H��', 0, 0 UNION SELECT 81, 5, '�e�}�s��', 0, 0 UNION SELECT 82, 5, '�ѥ]', 0, 0 UNION SELECT 83, 5, '�D��', 0, 0 UNION SELECT 84, 6, '�B', 0, 0 UNION SELECT 85, 6, '�Y���M', 0, 0 UNION SELECT 86, 6, '��', 0, 0 UNION SELECT 87, 6, '����', 0, 0 UNION SELECT 88, 6, '�Ȥy', 0, 0 UNION SELECT 89, 6, '�ȪM', 0, 0 UNION SELECT 90, 6, '�ȸJ', 0, 0 UNION SELECT 91, 6, '�Ⱥ�', 0, 0 UNION SELECT 92, 6, '�Ҥu��M', 0, 0 UNION SELECT 93, 6, '���e', 0, 0 UNION SELECT 94, 6, '�N�N�e', 0, 0 UNION SELECT 95, 6, '�N�N��', 0, 0 UNION SELECT 96, 6, '����', 0, 0";
		String sql6 = "CREATE TABLE CheckList_Hot_Food (Food_id INTEGER PRIMARY KEY, Types_id INTEGER, Name TEXT, Vote INTEGER, Report INTEGER)";
		String sql7 = "INSERT INTO CheckList_Hot_Food SELECT 1 as 'Food_id', 1 as Types_id, '�t�����h��' as Name, 0 as Vote, 0 as Report UNION SELECT 2, 1, '�V���\', 0, 0 UNION SELECT 3, 1, '�|�t�»���', 0, 0 UNION SELECT 4, 1, '�F�R��', 0, 0 UNION SELECT 5, 1, '��n�K����', 0, 0 UNION SELECT 6, 1, 'ʹ�}�ֳJ', 0, 0 UNION SELECT 7, 1, '�J�Ԥ�����', 0, 0 UNION SELECT 8, 1, '�����s����', 0, 0 UNION SELECT 9, 1, '���@�䦡�@����', 0, 0 UNION SELECT 10, 1, '�M��', 0, 0 UNION SELECT 11, 1, '�M���v�s', 0, 0 UNION SELECT 12, 1, '�ް��s', 0, 0 UNION SELECT 13, 1, '���X���J', 0, 0 UNION SELECT 14, 1, '�A�H�ѧ��l����', 0, 0 UNION SELECT 15, 1, '�A����', 0, 0 UNION SELECT 16, 1, '�Ŀ����Y��', 0, 0 UNION SELECT 17, 1, '�е�J�ԯ��z�{��', 0, 0 UNION SELECT 18, 1, '�ѳa��', 0, 0 UNION SELECT 19, 2, '���f��', 0, 0 UNION SELECT 20, 2, '������', 0, 0 UNION SELECT 21, 2, '���c', 0, 0 UNION SELECT 22, 2, '�ϥJ��', 0, 0 UNION SELECT 23, 2, '�ײ���', 0, 0 UNION SELECT 24, 2, '�Τ�', 0, 0 UNION SELECT 25, 2, '���³b���v��', 0, 0 UNION SELECT 26, 2, '������', 0, 0 UNION SELECT 27, 2, '�ޤU�C', 0, 0 UNION SELECT 28, 2, '�ޤj�z', 0, 0 UNION SELECT 29, 2, '�ޥ�', 0, 0 UNION SELECT 30, 2, '�ެ�', 0, 0 UNION SELECT 31, 2, '�޸y', 0, 0 UNION SELECT 32, 2, '���v�פ�', 0, 0 UNION SELECT 33, 2, '�޼�', 0, 0 UNION SELECT 34, 2, '�K��', 0, 0 UNION SELECT 35, 2, '�m����', 0, 0 UNION SELECT 36, 2, '�A����', 0, 0 UNION SELECT 37, 2, '�A�ި{', 0, 0 UNION SELECT 38, 2, '�A�ޯ��z', 0, 0 UNION SELECT 39, 2, '�A��', 0, 0 UNION SELECT 40, 2, '�A���l', 0, 0 UNION SELECT 41, 2, '�A�Z�z', 0, 0 UNION SELECT 42, 2, '���n��', 0, 0 UNION SELECT 43, 3, '��Į', 0, 0 UNION SELECT 44, 3, '�F�P��', 0, 0 UNION SELECT 45, 3, '��K��', 0, 0 UNION SELECT 46, 3, '�ὦ', 0, 0 UNION SELECT 47, 3, '����', 0, 0 UNION SELECT 48, 3, '�C�f', 0, 0 UNION SELECT 49, 3, '����F', 0, 0 UNION SELECT 50, 3, '�ӫ���', 0, 0 UNION SELECT 51, 3, '�۪�F', 0, 0 UNION SELECT 52, 3, '����', 0, 0 UNION SELECT 53, 3, '����', 0, 0 UNION SELECT 54, 3, '���Y', 0, 0 UNION SELECT 55, 3, '�H�ްF', 0, 0 UNION SELECT 56, 3, '�j��', 0, 0 UNION SELECT 57, 3, '�s��', 0, 0 UNION SELECT 58, 3, '�A�׳���', 0, 0 UNION SELECT 59, 3, '�A�a�l', 0, 0 UNION SELECT 60, 3, '�A��', 0, 0 UNION SELECT 61, 3, '�A�{��', 0, 0 UNION SELECT 62, 3, '�Q����', 0, 0 UNION SELECT 63, 3, '�ó��v', 0, 0 UNION SELECT 64, 3, '�ޤl', 0, 0 UNION SELECT 65, 3, '���', 0, 0 UNION SELECT 66, 4, '���L�]�ߤY', 0, 0 UNION SELECT 67, 4, '���Y', 0, 0 UNION SELECT 68, 4, '�פY', 0, 0 UNION SELECT 69, 4, '�ۤh���פY', 0, 0 UNION SELECT 70, 4, '�ʤ��ڤY', 0, 0 UNION SELECT 71, 4, '����', 0, 0 UNION SELECT 72, 4, '�»��A���Y', 0, 0 UNION SELECT 73, 4, '�����Y', 0, 0 UNION SELECT 74, 4, '�s���Y', 0, 0 UNION SELECT 75, 4, '���z�פY', 0, 0 UNION SELECT 76, 4, '���z�Z�x�Y', 0, 0 UNION SELECT 77, 4, '���Y', 0, 0 UNION SELECT 78, 4, '�ǳ��Y', 0, 0 UNION SELECT 79, 5, '�ͮ�', 0, 0 UNION SELECT 80, 5, '�˲�', 0, 0 UNION SELECT 81, 5, '���R', 0, 0 UNION SELECT 82, 5, '���G', 0, 0 UNION SELECT 83, 5, '�K��', 0, 0 UNION SELECT 84, 5, '�ۤh�z', 0, 0 UNION SELECT 85, 5, '������', 0, 0 UNION SELECT 86, 5, '�����', 0, 0 UNION SELECT 87, 5, '�����Y', 0, 0 UNION SELECT 88, 5, '���G', 0, 0 UNION SELECT 89, 5, '����', 0, 0 UNION SELECT 90, 5, '��׻�', 0, 0 UNION SELECT 91, 5, '���]', 0, 0 UNION SELECT 92, 5, '��l��', 0, 0 UNION SELECT 93, 5, '�z�J', 0, 0 UNION SELECT 94, 5, '�G��', 0, 0 UNION SELECT 95, 5, '������', 0, 0 UNION SELECT 96, 5, '�\��', 0, 0 UNION SELECT 97, 5, '�ǳ���', 0, 0 UNION SELECT 98, 5, '����', 0, 0 UNION SELECT 99, 6, '����', 0, 0 UNION SELECT 100, 6, '���Fۣ', 0, 0 UNION SELECT 101, 6, '�q��ۣ', 0, 0 UNION SELECT 102, 6, '��ۣ', 0, 0 UNION SELECT 103, 6, '�¤��', 0, 0 UNION SELECT 104, 6, '�j��ۣ', 0, 0 UNION SELECT 105, 6, '�A�Vۣ', 0, 0 UNION SELECT 106, 6, '����ۣ', 0, 0 UNION SELECT 107, 7, '�j����', 0, 0 UNION SELECT 108, 7, '�p�ŵ�', 0, 0 UNION SELECT 109, 7, '�V��', 0, 0 UNION SELECT 110, 7, '�յ�J', 0, 0 UNION SELECT 111, 7, '��v��', 0, 0 UNION SELECT 112, 7, '�o�e��', 0, 0 UNION SELECT 113, 7, '����', 0, 0 UNION SELECT 114, 7, '�z��', 0, 0 UNION SELECT 115, 7, '��͵�', 0, 0 UNION SELECT 116, 7, '�����', 0, 0 UNION SELECT 117, 7, '�A��', 0, 0 UNION SELECT 118, 7, '����', 0, 0 UNION SELECT 119, 7, '�Ե�', 0, 0 UNION SELECT 120, 7, '���', 0, 0 UNION SELECT 121, 7, '���', 0, 0 UNION SELECT 122, 7, '���¤�', 0, 0 UNION SELECT 123, 7, '���X', 0, 0 UNION SELECT 124, 7, '�����]', 0, 0 UNION SELECT 125, 7, '�S��', 0, 0 UNION SELECT 126, 7, '�ڽ�', 0, 0 UNION SELECT 127, 8, '���J��', 0, 0 UNION SELECT 128, 8, '�̯�', 0, 0 UNION SELECT 129, 8, '�Q�V', 0, 0 UNION SELECT 130, 8, '����', 0, 0 UNION SELECT 131, 8, '�����', 0, 0 UNION SELECT 132, 8, '���l��', 0, 0 UNION SELECT 133, 9, '�C����', 0, 0 UNION SELECT 134, 9, '�F����', 0, 0 UNION SELECT 135, 9, '�۳ªo', 0, 0 UNION SELECT 136, 9, '���Ȼ[', 0, 0 UNION SELECT 137, 9, '�C�㻶', 0, 0 UNION SELECT 138, 9, 'ʹ�}', 0, 0 UNION SELECT 139, 9, '���Ѵ�', 0, 0 UNION SELECT 140, 9, '�S�ų�����o', 0, 0 UNION SELECT 141, 9, '���sX.O.��', 0, 0 UNION SELECT 142, 9, '����', 0, 0 UNION SELECT 143, 10, '�T��', 0, 0 UNION SELECT 144, 10, '�˽��T��', 0, 0 UNION SELECT 145, 10, '��ʥ�', 0, 0 UNION SELECT 146, 10, '�L�\��', 0, 0 UNION SELECT 147, 10, '��s', 0, 0 UNION SELECT 148, 10, '�D��', 0, 0 UNION SELECT 149, 10, '�S�l��', 0, 0 UNION SELECT 150, 10, '��e', 0, 0 UNION SELECT 151, 10, '�ı���', 0, 0 UNION SELECT 152, 10, '�ȵ��S', 0, 0";
		String sql8 = "INSERT INTO CheckList_List SELECT 1 AS 'List_id',0 AS 'Int_id' ,'BBQ' AS 'List_Name',1 AS 'List_Type',0 AS 'Pw',0 AS 'Createtime' UNION SELECT 2, 0, 'HOTPOT', 2, 0, 0";
		String sql9 = "INSERT INTO CheckList_BbqListitems SELECT 1 AS 'List_id2', 59 AS 'Food_id', 0 AS 'Checkstate', 0 AS 'Price',1 AS 'Qty', 0 AS 'Send' UNION SELECT 1, 6, 0, 0, 1,0 UNION SELECT 1, 3, 0, 0, 1,0 UNION SELECT 1, 18, 0, 0, 1,0 UNION SELECT 1, 16, 0, 0, 1,0 UNION SELECT 1, 12, 0, 0, 1,0 UNION SELECT 1, 19, 0, 0, 1,0 UNION SELECT 1, 76, 0, 0, 1,0 UNION SELECT 1, 96, 0, 0, 1,0 UNION SELECT 1, 95, 0, 0, 1,0 UNION SELECT 1, 94, 0, 0, 1,0 UNION SELECT 1, 93, 0, 0, 1,0 UNION SELECT 1, 92, 0, 0, 1,0 UNION SELECT 1, 91, 0, 0, 1,0 UNION SELECT 1, 89, 0, 0, 1,0 UNION SELECT 1, 88, 0, 0, 1,0 UNION SELECT 1, 87, 0, 0, 1,0 UNION SELECT 1, 86, 0, 0, 1,0 UNION SELECT 1, 85, 0, 0, 1,0 UNION SELECT 1, 83, 0, 0, 1,0 UNION SELECT 1, 81, 0, 0, 1,0 UNION SELECT 1, 80, 0, 0, 1,0 UNION SELECT 1, 73, 0, 0, 1,0 UNION SELECT 1, 71, 0, 0, 1,0 UNION SELECT 1, 64, 0, 0, 1,0 UNION SELECT 1, 65, 0, 0, 1,0 UNION SELECT 1, 40, 0, 0, 1,0 UNION SELECT 1, 57, 0, 0, 1,0 UNION SELECT 1, 38, 0, 0, 1,0 UNION SELECT 1, 35, 0, 0, 1,0 UNION SELECT 1, 33, 0, 0, 1,0 UNION SELECT 1, 31, 0, 0, 1,0 UNION SELECT 1, 30, 0, 0, 1,0 UNION SELECT 1, 28, 0, 0, 1,0 UNION SELECT 1, 24, 0, 0, 1,0;";
		String sql10 ="INSERT INTO CheckList_HotListitems SELECT 2 AS 'List_id2', 144 AS 'Food_id', 0 AS 'Checkstate', 0 AS 'Price',1 AS 'Qty', 0 AS 'Send' UNION SELECT 2, 143, 0, 0, 1,0 UNION SELECT 2, 136, 0, 0, 1,0 UNION SELECT 2, 137, 0, 0, 1,0 UNION SELECT 2, 138, 0, 0, 1,0 UNION SELECT 2, 129, 0, 0, 1,0 UNION SELECT 2, 110, 0, 0, 1,0 UNION SELECT 2, 115, 0, 0, 1,0 UNION SELECT 2, 113, 0, 0, 1,0 UNION SELECT 2, 118, 0, 0, 1,0 UNION SELECT 2, 96, 0, 0, 1,0 UNION SELECT 2, 93, 0, 0, 1,0 UNION SELECT 2, 84, 0, 0, 1,0 UNION SELECT 2, 82, 0, 0, 1,0 UNION SELECT 2, 73, 0, 0, 1,0 UNION SELECT 2, 72, 0, 0, 1,0 UNION SELECT 2, 71, 0, 0, 1,0 UNION SELECT 2, 60, 0, 0, 1,0 UNION SELECT 2, 53, 0, 0, 1,0 UNION SELECT 2, 48, 0, 0, 1,0 UNION SELECT 2, 26, 0, 0, 1,0 UNION SELECT 2, 24, 0, 0, 1,0 UNION SELECT 2, 4, 0, 0, 1,0";

		db.execSQL(sql);
		db.execSQL(sql2);
		db.execSQL(sql3);
		db.execSQL(sql4);
		db.execSQL(sql5);
		db.execSQL(sql6);
		db.execSQL(sql7);
		db.execSQL(sql8);
		db.execSQL(sql9);
		db.execSQL(sql10);
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
		
	}

}