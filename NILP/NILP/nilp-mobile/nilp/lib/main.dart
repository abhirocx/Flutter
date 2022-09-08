import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_localizations/flutter_localizations.dart';
import 'package:flutter_windowmanager/flutter_windowmanager.dart';
import 'package:nilp/screens/splash/splash.dart';
import 'package:nilp/utility/Util.dart';

import 'localization/locale_constant.dart';
import 'localization/localizations_delegate.dart';

void main() async {
  SystemChrome.setPreferredOrientations([
    DeviceOrientation.portraitUp,
  ]);
  runApp(MyApp());
}

class MyApp extends StatefulWidget {
  static void setLocale(BuildContext context, Locale newLocale) {
    var state = context.findAncestorStateOfType<_MyAppState>();
    state!.setLocale(newLocale);
  }

  @override
  State<StatefulWidget> createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  Locale _locale = const Locale('en', '');

  @override
  initState() {
    flagSecure();
    super.initState();
  }

  flagSecure() async {
    await FlutterWindowManager.addFlags(FlutterWindowManager.FLAG_SECURE);
  }

  void setLocale(Locale locale) {
    setState(() {
      _locale = locale;
    });
  }

  @override
  void didChangeDependencies() async {
    getLocale().then((locale) {
      setState(() {
        _locale = locale;
        if (_locale.languageCode == 'en') {
          Util.currentLocal = "en";
        } else if (_locale.languageCode == 'gu') {
          Util.currentLocal = "guj";
        } else if (_locale.languageCode == 'as') {
          Util.currentLocal = "asm";
        } else if (_locale.languageCode == 'ks') {
          Util.currentLocal = "ks";
        } else if (_locale.languageCode == 'kn') {
          Util.currentLocal = "kan";
        } else if (_locale.languageCode == 'mr') {
          Util.currentLocal = "mar";
        } else if (_locale.languageCode == 'ml') {
          Util.currentLocal = "mal";
        } else if (_locale.languageCode == 'pa') {
          Util.currentLocal = "pan";
        } else if (_locale.languageCode == 'te') {
          Util.currentLocal = "tel";
        } else if (_locale.languageCode == 'hi') {
          Util.currentLocal = "hin";
        } else {
          Util.currentLocal = "en";
        }
      });
    });
    super.didChangeDependencies();
  }

  @override
  Widget build(BuildContext context) {
    SystemChrome.setPreferredOrientations([
      DeviceOrientation.portraitUp,
      DeviceOrientation.portraitDown,
    ]);
    return MaterialApp(
      debugShowCheckedModeBanner: false,

      title: 'Onboarding Demo',
      theme: ThemeData(
        primarySwatch: Colors.blue,
      ),
      locale: _locale,
      home: Splash(),
      // builder: (context, child) {
      //   final scale = MediaQuery.of(context).textScaleFactor.clamp(1.0, 1.0);
      //   return MediaQuery(data: MediaQuery.of(context).copyWith(textScaleFactor: scale), child: child!);
      // },
      // home:DashBoard(),
      // home:ExpansionTileCardDemo(),
      supportedLocales: const [
        Locale('en', ''),
        Locale('gu', ''),
        Locale('as', ''),
        Locale('ks', ''),
        Locale('kn', ''),
        Locale('mr', ''),
        Locale('ml', ''),
        Locale('pa', ''),
        Locale('te', ''),
        Locale('hi', '')
      ],
      localizationsDelegates: const [
        AppLocalizationsDelegate(),
        GlobalMaterialLocalizations.delegate,
        GlobalWidgetsLocalizations.delegate,
        GlobalCupertinoLocalizations.delegate,
      ],
      localeResolutionCallback: (locale, supportedLocales) {
        for (var supportedLocale in supportedLocales) {
          if (supportedLocale.languageCode == locale?.languageCode &&
              supportedLocale.countryCode == locale?.countryCode) {
            return supportedLocale;
          }
        }
        return supportedLocales.first;
      },
    );
  }
}
