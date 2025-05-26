# pip install spacy
# python -m spacy download pt_core_news_sm
# python -m spacy download en_core_web_sm
# pip install langdetect


'''
Comandos para testes:

python3 indexador/lematizar.py "Canções lindas foram escritas por compositores talentosos."

python3 indexador/lematizar.py "Gatos bonitos estão pulando sobre cadeiras"

'''

import spacy
import sys
from langdetect import detect

# Carregar modelos para ambos os idiomas
def main():    
    try:
        import spacy
        from langdetect import detect
        
        nlp_pt = spacy.load("pt_core_news_sm")
        nlp_en = spacy.load("en_core_web_sm")

        if len(sys.argv) > 1:
            input_text = ' '.join(sys.argv[1:])
        else:
            input_text = sys.stdin.read().strip()

        if not input_text:
            print("ERRO: Texto de entrada vazio", file=sys.stderr)
            sys.exit(1)

        lang = detect(input_text)
        nlp = nlp_pt if lang == 'pt' else nlp_en
        doc = nlp(input_text)
        
        lemmas = [token.lemma_.lower() for token in doc 
                 if not token.is_stop and not token.is_punct and token.text.strip()]
        
        if len(sys.argv) > 1:
            print('\n'.join(lemmas))    
        else:
            sys.stdout.write('\n'.join(lemmas)) 
        
    except Exception as e:
        traceback.print_exc(file=sys.stderr)
        sys.exit(1)   

if __name__ == "__main__":
    main()