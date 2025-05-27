


'''

Para instalação:

Configurar o venv (virtualenviroment)

pip install spacy

python -m spacy download pt_core_news_sm

python -m spacy download en_core_web_sm

pip install langdetect


Comandos para testes:

python3 indexador/lematizar.py "Canções lindas foram escritas por compositores talentosos."

python3 indexador/lematizar.py "Gatos bonitos estão pulando sobre cadeiras"

'''

import spacy
import sys
import traceback
from langdetect import detect, DetectorFactory, LangDetectException
from spacy.lang.pt.stop_words import STOP_WORDS as PT_STOP_WORDS
from spacy.lang.en.stop_words import STOP_WORDS as EN_STOP_WORDS

# Para consistência na detecção de idioma
DetectorFactory.seed = 0

# Modelos carregados uma única vez
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
            # fallback para inglês se idioma não suportado
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

def process_text(text):
    try:
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
    except Exception as e:
        print("Erro no processamento:", e, file=sys.stderr)
        raise

def main():
    try:
        input_text = ' '.join(sys.argv[1:]) if len(sys.argv) > 1 else sys.stdin.read().strip()

        if not input_text:
            print("ERRO: Texto de entrada vazio", file=sys.stderr)
            sys.exit(1)

        lemmas = process_text(input_text)
        output = '\n'.join(lemmas)

        if len(sys.argv) > 1:
            print(output)
        else:
            sys.stdout.write(output)

    except Exception as e:
        print(f"Erro fatal: {str(e)}", file=sys.stderr)
        sys.exit(1)

if __name__ == "__main__":
    main()
