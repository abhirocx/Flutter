import 'package:flutter/material.dart';
import 'language/language_asm.dart';
import 'language/language_bod.dart';
import 'language/language_doi.dart';
import 'language/language_en.dart';
import 'language/language_guj.dart';
import 'language/language_hi.dart';
import 'language/language_kas.dart';
import 'language/language_kn.dart';
import 'language/language_mar.dart';
import 'language/language_ml.dart';
import 'language/language_pan.dart';
import 'language/language_tel.dart';
import 'language/languages.dart';

class AppLocalizationsDelegate extends LocalizationsDelegate<Languages> {
  const AppLocalizationsDelegate();

  @override
  bool isSupported(Locale locale) => ['en', 'hi' ,'gu','as',
    //'bo','doi',
    'ks',
    'kn',
    'mr',
  'ml','pa','te'].contains(locale.languageCode);

  @override
  Future<Languages> load(Locale locale) => _load(locale);

  static Future<Languages> _load(Locale locale) async {
    switch (locale.languageCode) {
      case 'en':
        return LanguageEn();
      case 'hi':
        return LanguageHi();
      case 'gu':
        return LanguageGuj();
      case 'as':
        return LanguageAsm();
      case 'bo':
        return LanguageBod();
      case 'doi':
        return LanguageDoi();
      case 'ks':
        return LanguageKas();
      case 'kn':
        return LanguageKn();
      case 'mr':
        return LanguageMar();
      case 'ml':
        return LanguageMl();
      case 'pa':
        return LanguagePan();
      case 'te':
        return LanguageTel();
      default:
        return LanguageEn();
    }
  }

  @override
  bool shouldReload(LocalizationsDelegate<Languages> old) => false;
}
