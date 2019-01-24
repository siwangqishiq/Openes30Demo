#include "SQLiteUtil.h"

#include <vector>

char * SQLiteUtil::pErrMsg = 0;

sqlite3* SQLiteUtil::pDB = 0;

int SQLiteUtil::nRes = 0;
//��ѯ������������
std::vector<std::string>* SQLiteUtil::resultVector = new std::vector<std::string>();
SQLiteUtil::SQLiteUtil()
{

}
//��openSQLiteDB
bool SQLiteUtil::open()
{
	//��·������utf-8����
	//���·���а������ģ���Ҫ���б���ת��
	const char* const dbname = "/data/data/com.bn.bullet7111/macydb";
	//�򿪵Ĵ�����
	nRes = sqlite3_open(dbname, &pDB);

	if(nRes != SQLITE_OK )
	{
		sqlite3_free(pErrMsg);
		//�ر����ݿ�
		sqlite3_close(pDB);
		return false;
	}
	return true;
}
void SQLiteUtil::close()
{
	sqlite3_close(pDB);
}
int SQLiteUtil::doSqlite3_exec(std::string SQLSring)
{
	int result = 0;
	//�����ݿ�
	open();
	//ִ��SQL���
	result = sqlite3_exec(pDB, SQLSring.c_str(), 0, 0, &pErrMsg );
	//�ر����ݿ�
	close();
	return result;
}
std::vector<std::string>* SQLiteUtil::query(std::string sql)
{
	//��ռ�¼
	resultVector->clear();
	int result = 0;
	//�����ݿ�
	open();
	//��ѯ����
	result = sqlite3_exec(pDB, sql.c_str(), _sql_callback, 0, &pErrMsg);

	//�ر����ݿ�
	close();
	return resultVector;
}
int SQLiteUtil::_sql_callback(void * notused, int argc, char** argv, char** szColName)
{
	for (int i=0; i < argc; i++ )
	{
		std::string temp = argv[i];
		//��¼���
		resultVector->push_back(std::vector<std::string>::value_type(temp));
	}
	return 0;
}
