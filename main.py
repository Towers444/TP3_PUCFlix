'''
Instruções:

pip install -r requirements.txt
uvicorn main:app --reload --host 0.0.0.0 --port 8000 

'''

from fastapi import FastAPI, HTTPException
from pydantic import BaseModel
from indexador.script import process_text

app = FastAPI()

class TextInput(BaseModel):
    text: str

@app.post("/process")
def process(input: TextInput):
    if not input.text.strip():
        raise HTTPException(status_code=400, detail="Texto vazio")
    
    try:
        result = process_text(input.text)
        return result
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))
