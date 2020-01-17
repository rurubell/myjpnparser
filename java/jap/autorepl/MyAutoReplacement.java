package src.jap.autorepl;


public interface MyAutoReplacement
{
	//Удалить замену для строки
	public void deleteAutoReplacement( String s_key_string );
	//Узнать ключ для автозамены
	public String getKeyForAutoReplacementValue( String s_ar_value );
	//Обработать текст
	public String handleText( String s_text );
	//Добавить/Изменить замены для символа/строки
	public void changeOrAddSimilar( String s_str, String s_similar_str );
	//Сохранить список в файл
	public void saveToFile();
	public void saveToFile( String s_path );
	//Напечатать весь список
	public void show();
	//Проверка строки на корректность
	public boolean isCorrectString( String s_string );
}
