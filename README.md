# Tetris

##機能

以下の機能を実装しました。

###GameOver  
仕様：画面外までブロックが重なるとゲームオーバーになる  
実装はBoard.javaのgameOverメソッドからMainActivity.javaのonGameOverメソッドの呼び出しです  

###LongClick  
仕様：ボタンの長押しでブロックのスピードが変わる  
実装はMainActivity.javaのgameButtonClickメソッドの内容を変更しました  

###BestScore  
仕様：最高スコアを保持する  
実装はBoard.javaのgameOverメソッドからMainActivity.javaのsaveBestScoreメソッドの呼び出しです  
