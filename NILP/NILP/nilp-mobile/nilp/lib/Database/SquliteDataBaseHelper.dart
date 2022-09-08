import 'dart:async';
import 'dart:io' as io;
import 'package:path/path.dart';
import 'package:path_provider/path_provider.dart';
import 'package:sqflite/sqflite.dart';
import '../model/DBModel/HeadModel.dart';
import '../model/DBModel/MemberModel.dart';
import '../model/DBModel/localHeadUpdateModel.dart';
import '../model/FamilyInfo.dart';

class SquliteDatabaseHelper {
  static final SquliteDatabaseHelper _instance =
      new SquliteDatabaseHelper.internal();
  factory SquliteDatabaseHelper() => _instance;

  static Database? _db;

  Future<Database?> get db async {
    if (_db != null) return _db;
    _db = await initDb();
    return _db;
  }

  SquliteDatabaseHelper.internal();

  initDb() async {
    io.Directory documentsDirectory = await getApplicationDocumentsDirectory();
    String path = join(documentsDirectory.path, "main.db");
    var theDb = await openDatabase(path, version: 1, onCreate: _onCreate);
    return theDb;
  }

  void _onCreate(Database db, int version) async {
    await db.execute('''
                CREATE TABLE IF NOT EXISTS head( 
                       _id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        name varchar(255) not null,
                        address varchar(255) not null,
                        mark_as varchar(255) not null,
                        age varchar(255) not null,
                        mobile varchar(255) not null,
                        gender varchar(255) not null,
                        proof_type varchar(255) not null,
                        proof_detail varchar(255) not null,
                        social_category_id varchar(255) not null,
                        father_name varchar(255) not null,
                        profession varchar(255) not null,
                        vt_type varchar(255) not null,
                        is_only_head int not null,
                        is_divyang int not null,
                        qualification varchar(255) not null
                   ); ''');
    await db.execute('''CREATE TABLE IF NOT EXISTS member( 
                        member_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        head_id INTEGER,
                        member_name varchar(255) not null,
                        member_address varchar(255) not null,
                        member_mark_as varchar(255) not null,
                        member_age varchar(255) not null,
                        member_mobile varchar(255) not null,
                        member_gender varchar(255) not null,
                        member_proof_type varchar(255) not null,
                        member_proof_detail varchar(255) not null,
                        member_social_category_id varchar(255) not null,
                        member_father_name varchar(255) not null,
                        member_relation varchar(255) not null,
                        member_vt_type varchar(255) not null,
                        member_profession varchar(255) not null,
                        member_qualification varchar(255) not null,
                        member_divyang int not null,
                        FOREIGN KEY(head_id) REFERENCES head(_id)
                   );
               ''');
  }

  Future<int?> saveHead(HeadModel user) async {
    var dbClient = await db;
    int res = await dbClient!.insert("head", user.toJson());
    print("dssfhsj1,$res");
    return res;
  }

  Future<int?> saveMember(MemberModel user, int head_id) async {
    var dbClient = await db;
    int res = await dbClient!.insert("member", user.toJson());
    return res;
  }

  Future<List<Family_Head>?> getUser() async {
    var dbClient = await db;
    List<int> IDs = <int>[];
    List<Family_Head> lsthead = [];
    List<Members>? lstMembers = [];
    late Family_Head family_head;
    List<Map<String, dynamic>> maps = await dbClient!.query("head");
    if (maps.length > 0) {
      for (Map incomingMap in maps) {
        IDs = [];
        IDs.add(incomingMap['_id']);

        family_head = new Family_Head();
        family_head.id = incomingMap['_id'];
        family_head.name = incomingMap['name'];
        family_head.gender = incomingMap['gender'];
        family_head.address = incomingMap['address'];
        family_head.fatherName = incomingMap['father_name'];
        family_head.age = incomingMap['age'];
        family_head.markAs = incomingMap['mark_as'];

        family_head.mobile = incomingMap['mobile'];
        family_head.profession = incomingMap['profession'];
        family_head.proofDetail = incomingMap['proof_detail'];
        family_head.proofType = incomingMap['proof_type'];
        family_head.vtType = incomingMap['vt_type'];
        family_head.qualification = incomingMap['qualification'];
        family_head.isOnlyHead = incomingMap['is_only_head'];
        family_head.socialCategoryId = incomingMap['social_category_id'];

        print("vvxnb");

        family_head.isdivyang = incomingMap['is_divyang'];

        print("vvxnbasfajb");


        lstMembers = await getMembers(IDs[0]);
        family_head.members = lstMembers;
        lsthead.add(family_head);
      }
      return lsthead;
    } else {
      return lsthead;
    }
  }


  Future<int> deleteMemberUsers(int id) async {
    var dbClient = await db;
    int res = await dbClient!
        .rawDelete('DELETE FROM member WHERE member_id = ?', ['$id']);
    return res;
  }

  Future<int> deleteHeadUsers(int id) async {
    var dbClient = await db;
    int res =
        await dbClient!.rawDelete('DELETE FROM head WHERE _id = ?', ['$id']);
    return res;
  }

  Future<int?> updateHeadData(LocalHeadUpdateModel data, int id) async {
    var dbClient = await db;
    var result = await dbClient!
        .update("head", data.toMap(), where: '_id = ?', whereArgs: [id]);
    return result;
  }

  Future<int?> updateMemberData(Members data, int id) async {
    var dbClient = await db;
    var result = await dbClient!.update("member", data.toMap(),
        where: 'member_id = ?', whereArgs: [id]);
    return result;
  }


  Future<List<Members>?> getMembers(int id) async {
    var dbClient = await db;

    List<Map<String, dynamic>> result = await dbClient!
        .query('member', where: "head_id =? ", whereArgs: ['$id']);
    List<Members>? lstMembers = [];
    Members members;
    if (result.length > 0) {
      for (Map incomingMap in result) {
        members = new Members();
        members.member_id = incomingMap['member_id'];
        members.head_id = incomingMap['head_id'];
        members.memberName = incomingMap['member_name'];
        members.memberAddress = incomingMap['member_address'];
        members.memberAge = incomingMap['member_age'];
        members.memberFatherName = incomingMap['member_father_name'];
        members.memberGender = incomingMap['member_gender'];
        members.memberMarkAs = incomingMap['member_mark_as'];
        members.memberMobile = incomingMap['member_mobile'];
        members.memberProfession = incomingMap['member_profession'];
        members.memberProofDetail = incomingMap['member_proof_detail'];
        members.memberProofType = incomingMap['member_proof_type'];
        members.memberQualification = incomingMap['member_qualification'];
        members.memberRelation = incomingMap['member_relation'];
        members.memberDivyang = incomingMap['member_divyang'];
        members.memberSocialCategoryId =
            incomingMap['member_social_category_id'];
        members.memberVtType = incomingMap['member_vt_type'];
        lstMembers.add(members);
      }
      return lstMembers;
    } else {
      members = new Members();
      lstMembers.add(members);
      return lstMembers;
    }
  }
}

