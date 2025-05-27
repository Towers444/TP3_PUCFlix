import spacy
from langdetect import detect, DetectorFactory, LangDetectException
from spacy.lang.pt.stop_words import STOP_WORDS as PT_STOP_WORDS
from spacy.lang.en.stop_words import STOP_WORDS as EN_STOP_WORDS

DetectorFactory.seed = 0

MODELS = {}
STOP_WORDS = {
    'pt': PT_STOP_WORDS,
    'en': EN_STOP_WORDS,
}

def load_model(lang):
    if lang not in MODELS:
        if lang == 'pt':
            MODELS['pt'] = spacy.load("pt_core_news_sm", disable=['parser', 'ner'])
        elif lang == 'en':
            MODELS['en'] = spacy.load("en_core_web_sm", disable=['parser', 'ner'])
        else:
            MODELS['en'] = spacy.load("en_core_web_sm", disable=['parser', 'ner'])
            lang = 'en'
    return MODELS.get(lang, MODELS['en'])

def detect_language(text):
    if len(text) < 10:
        return 'en'
    try:
        lang = detect(text)
        return lang if lang in STOP_WORDS else 'en'
    except LangDetectException:
        return 'en'

def process_text(text: str) -> list:
    lang = detect_language(text)
    nlp = load_model(lang)
    current_stop_words = STOP_WORDS.get(lang, EN_STOP_WORDS)

    doc = nlp(text)
    return [
        token.lemma_.lower()
        for token in doc
        if not token.is_stop and
           not token.is_punct and
           token.text.strip() and
           token.lemma_.lower() not in current_stop_words
    ]
