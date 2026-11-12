export class Atachment {
    constructor(
        public contentType: string = "",
        public language: string = "",
        public data: string = "",
        public url: string = "",
        public size: number = 0,
        public hash: string = "",
        public title: string = "",
        public creation: Date = new Date()
    ) { }
}