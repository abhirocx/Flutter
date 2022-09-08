class LanguageData {
  final String flag;
  final String name;
  final String languageCode;

  LanguageData(this.flag, this.name, this.languageCode);

  static List<LanguageData> languageList() {
    return <LanguageData>[
      LanguageData("🇺🇸", "English", 'en'),
      LanguageData("🇮🇳", "Gujrati", 'gu'),
      LanguageData("🇮🇳", "Assami", 'as'),
      // LanguageData("🇮🇳", "Bodo", 'bo'),
      // LanguageData("🇮🇳", "Dogri", 'doi'),
       LanguageData("🇮🇳", "Kashmiri", 'ks'),
      LanguageData("🇮🇳", "Kannada", 'kn'),
      LanguageData("🇮🇳", "Marathi", 'mr'),
      LanguageData("🇮🇳", "Malayalam", 'ml'),
      LanguageData("🇮🇳", "Panjabi", 'pa'),
      LanguageData("🇮🇳", "Telugu", 'te'),
      LanguageData("🇮🇳", "हिंदी", 'hi'),
    ];
  }
}
